package com.asherfischbaum.hackapint;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

/**
 * Created by Andrea on 07/02/2016.
 */
public class Matcher {

    private String RECEIVER_KEY, SENDER_KEY;

    public Matcher(String receiverkey, String senderKey){
        this.RECEIVER_KEY = receiverkey;
        this.SENDER_KEY = senderKey;
    }

    static final Firebase firebase= new Connections().getFireDB();

    public void sendRequest(){
        //this user sends request to map with user with key receiverKey
        firebase.child("users").child(RECEIVER_KEY).child("requestFrom").setValue(SENDER_KEY);
    }

    public boolean checkMutual(){

        boolean match = ((firebase.child("users").child(RECEIVER_KEY).child("requestFrom").getKey().equals(SENDER_KEY))&&
                (firebase.child("users").child(SENDER_KEY).child("requestFrom").getKey().equals(RECEIVER_KEY)));

        //boolean agecheck = ((firebase.child("users").child(RECEIVER_KEY).child("age").getKey() >= 18)&&firebase.child

        return match;
    }

    //run the check if there is a change in the key
    public void runtheCheck(){
        firebase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                //if(!dataSnapshot.child("users").child(RECEIVER_KEY).child("requestedForm").getValue().equals(s)){
                if(checkMutual()){
                    
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
