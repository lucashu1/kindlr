package com.example.team19.kindlr;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public abstract class FirebaseAccessor<T> {
    private FirebaseDatabase database;
    private DatabaseReference dataRef;
    private Map<String, T> itemsMap;
    private Class<T> typeParamClass;

    private final static String LOG_TAG = "FirebaseAccessor";

    private Boolean initialized;

    public FirebaseAccessor(Class<T> typeParamClass) {
        initialized = false;
        this.typeParamClass = typeParamClass;
    }

    public boolean doesItemExist(String itemId) {
        return (itemsMap.containsKey(itemId));
    }

    public void deleteItem(String itemId) {
        if (!doesItemExist(itemId))
            return;

        itemsMap.remove(itemId);
        DatabaseReference itemRef = dataRef.child(itemId);
        itemRef.removeValue();
    }

    public void saveToFirebase() {
        dataRef.setValue(itemsMap);
    }

    protected Map<String, T> getItemsMap() {
        return itemsMap;
    }

    protected void updateChild(String id) {
        dataRef.child(id).setValue(itemsMap.get(id));
    }

    protected abstract String getFirebaseRefName();


    protected void startDbRead() {
        database  = FirebaseDatabase.getInstance();
        dataRef = database.getReference(this.getFirebaseRefName());

        Log.d(LOG_TAG, "Got data ref");

        // On data change, re-read booksMap from the database
        dataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(LOG_TAG, "Firebase on data change event fired");
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                itemsMap = new HashMap<String, T>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    T book = snapshot.getValue(typeParamClass);
                    itemsMap.put(snapshot.getKey(), book);
                }

                onFinishDbRead();
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("WARN", "Failed to read value.", error.toException());
            }
        });
    }

    public T getItemByID(String itemId) {
        if (!itemsMap.containsKey(itemId))
            return null;
        return itemsMap.get(itemId);
    }

    protected String getInsertKey() {
        return  dataRef.push().getKey();
    }

    protected void onFinishDbRead() {
        Log.d(LOG_TAG, "Calling back on DB read");
        synchronized(initialized) {
            initialized.notify();
        }
    }

    public void removeAllItems() {
        dataRef.removeValue();
    }

    public final void initialize() {
        initialize(false);
    }


    public final void initialize(boolean shouldWait) {
        Log.d(LOG_TAG, "Starting DB read for " + this.getFirebaseRefName());
        startDbRead();
        Log.d(LOG_TAG, "Sent DB read");

        if (shouldWait) {
            synchronized (initialized) {
                try {
                    initialized.wait();
                } catch (InterruptedException e) {
                    // Happens if someone interrupts your thread.
                }
            }
        }

        Log.d(LOG_TAG, "Finished reading from DB for " + this.getFirebaseRefName());
    }
}
