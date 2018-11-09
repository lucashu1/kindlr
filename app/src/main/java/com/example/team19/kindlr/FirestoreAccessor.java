package com.example.team19.kindlr;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.WriteBatch;

import java.util.HashMap;
import java.util.Map;


public abstract class FirestoreAccessor<T> {

    private Map<String, T> itemsMap;
    private Class<T> typeParamClass;
    private String firestoreCollection;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final static String TAG = "FirestoreAccessor";

    private Boolean initialized;

    public FirestoreAccessor(Class<T> typeParamClass, String firestoreCollection) {
        initialized = false;
        this.typeParamClass = typeParamClass;
        this.firestoreCollection = firestoreCollection;
    }

    private String getCollectionName() {
        return firestoreCollection;
    }

    private CollectionReference getCollection() {
        return this.db.collection(this.firestoreCollection);
    }

    private DocumentReference getDocument(String id) {
        return this.getCollection().document(id);
    }

    private void clearMap() {
        this.itemsMap.clear();
    }

    public boolean doesItemExist(String itemId) {
        return (itemsMap.containsKey(itemId));
    }

    public String getInsertKey() {
        return this.getCollection().document().getId();
    }

    public String addItem(T pojo) {
        String id = this.getInsertKey();
        Log.d(TAG, "Adding document: " + id);
        this.getCollection().document(id).set(pojo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot added");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });

        return id;
    }

    protected Map<String, T> getItemsMap() {
        return itemsMap;
    }

    public boolean deleteItem(String itemId) {
        if (!doesItemExist(itemId))
            return false;

        Log.d(TAG, "Deleting document: " + itemId);
        this.getCollection().document(itemId)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });

        return true;
    }

    protected void updateChild(String id) {
        updateItem(id);
    }

    protected void updateItem(String id) {
        if (!doesItemExist(id))
            return;

        this.getCollection().document(id).set(itemsMap.get(id))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
    }

    public T getItemByID(String itemId) {
        if (!itemsMap.containsKey(itemId))
            return null;
        return itemsMap.get(itemId);
    }

    // Remove all items currently in the itemsMap from firestore
    public void removeAllItems() {
        // Prepare batch delete
        WriteBatch batch = db.batch();
        for (Map.Entry<String, T> entry : this.itemsMap.entrySet()) {
            String id = entry.getKey();
            batch.delete(this.getDocument(id));
        }

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "Removed all items for " + getCollectionName() + " from firestore");
            }
        });

        // Clear map
        this.clearMap();
    }

    public final void initialize() {
        initialize(false);
    }

    protected void onFinishDbRead() {
        Log.d(TAG, "Calling back on DB read");
        synchronized(initialized) {
            initialized.notify();
        }
    }

    public final void initialize(boolean shouldWait) {
        Log.d(TAG, "Starting DB read for " + this.firestoreCollection);
        this.refresh();
        Log.d(TAG, "Sent DB read");

        if (shouldWait) {
            synchronized (initialized) {
                try {
                    initialized.wait();
                } catch (InterruptedException e) {
                    // Happens if someone interrupts your thread.
                }
            }
        }

        Log.d(TAG, "Finished reading from DB for " + this.firestoreCollection);
    }

    public final void refresh() {
        this.getCollection()
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        itemsMap = new HashMap<String, T>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            T obj = document.toObject(typeParamClass);
                            itemsMap.put(document.getId(), obj);
                            Log.d(TAG, document.getId() + " => " + document.getData());
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                    onFinishDbRead();
                }
            });
    }

    // TODO: add real-time listening? https://firebase.google.com/docs/firestore/query-data/listen

}
