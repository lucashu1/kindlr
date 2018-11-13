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
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final static String TAG = "FirestoreAccessor";

    private Boolean initialized;

    public FirestoreAccessor(Class<T> typeParamClass) {
        initialized = false;
        this.typeParamClass = typeParamClass;
        this.itemsMap = new HashMap<String, T>();
    }

    // Get name of this Collection (similar to a top-level Reference)
    abstract String getFirestoreCollectionName();

    // Get Firestore CollectionReference
    private CollectionReference getCollection() {
        return this.db.collection(getFirestoreCollectionName());
    }

    // Get Firestore DocumentReference (elements of Collection)
    private DocumentReference getDocument(String id) {
        return this.getCollection().document(id);
    }

    // Re-init itemsMap
    private void clearMap() {
        this.itemsMap = new HashMap<String, T>();
    }

    // Check if item in map
    public boolean doesItemExist(String itemId) {
        return (itemsMap.containsKey(itemId));
    }

    // Get a unique insert key/id
    public String getInsertKey() {
        return this.getCollection().document().getId();
    }

    // Add item to map, and to firestore
    public String putItem(String id, T pojo) {
        this.itemsMap.put(id, pojo);
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

    // Return entire itemsMap
    protected Map<String, T> getItemsMap() {
        return itemsMap;
    }

    // Delete specific item from map, and from Firestore
    public boolean deleteItem(String itemId) {
        if (!doesItemExist(itemId))
            return false;

        itemsMap.remove(itemId);

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

    // UpdateChild from map
    protected void updateChildFromMap(String id) {
        if (!doesItemExist(id))
            return;

        // Update in Firestore
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

    // Return item by ID from map
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
                Log.d(TAG, "Removed all items for " + getFirestoreCollectionName() + " from firestore");
            }
        });

        // Clear map
        this.clearMap();
    }

    // Alias for addOrUpdateAllItems()
    public void saveToFirebase() {
        this.addOrUpdateAllItems();
    }

    // Add all items in itemsMap to Firestore
    protected void addOrUpdateAllItems() {
        // Prepare batch delete
        WriteBatch batch = db.batch();
        for (Map.Entry<String, T> entry : this.itemsMap.entrySet()) {
            String id = entry.getKey();
            T obj = entry.getValue();
            batch.set(this.getDocument(id), obj);
        }

        // Commit the batch
        batch.commit().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d(TAG, "Added/updated all items for " + getFirestoreCollectionName() + " to firestore");
            }
        });
    }

    public final void initialize() {
        initialize(false);
    }

    // Release initialized lock
    protected void onFinishDbRead() {
        Log.d(TAG, "Calling back on DB read");
        synchronized(initialized) {
            initialized.notify();
        }
    }

    // Initial read from DB (blocking/locked)
    public final void initialize(boolean shouldWait) {
        Log.d(TAG, "Starting DB read for " + getFirestoreCollectionName());
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

        Log.d(TAG, "Finished reading from DB for " + getFirestoreCollectionName());
    }

    // Default refresh: don't erase existing entries
    public final void refresh() {
        refresh(false);
    }

    // Wipe map and read from Firestore
    public final void refresh(boolean eraseExisting) {
        // create new map before reading from DB --> erases existing entries
        if (eraseExisting == true) {
            this.getCollection()
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                itemsMap = new HashMap<String, T>(); // create new hashmap
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

        // don't create new map -- don't delete existing entries
        else {
            this.getCollection()
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
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

    }

    // TODO: add real-time listening? https://firebase.google.com/docs/firestore/query-data/listen

}
