package edu.elsmancs.pigcoin;

import java.security.PublicKey;

public class Transaction {

    private String hash = null;
    private String prevHash = null;
    private PublicKey pKeySender = null;
    private PublicKey pKeyReceiver = null;
    private double pigcoins = 0d;
    private String message = null;
    private byte[] signature = {};


    public Transaction() {}

    public Transaction(String hash, String prevHash, PublicKey sender, PublicKey receiver, double pigcoins, String message) {
        this.hash = hash;
        this.prevHash = prevHash;
        this.pKeySender = sender;
        this.pKeyReceiver = receiver;
        this.pigcoins = pigcoins;
        this.message = message;
    }


    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

    public byte[] getSignature() {
        return signature;
    }

    public String getHash() {
        return hash;
    }

    public String getPrevHash() {
        return prevHash;
    }

    public PublicKey getPKeySender() {
        return pKeySender;
    }

    public PublicKey getPKeyReceiver() {
        return pKeyReceiver;
    }

    public double getPigcoins() {
        return pigcoins;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {

        return "\n" + "Hash = " + getHash() + "\n" +
                "Prev_hash = " + getPrevHash() + "\n" +
                "pKey_sender = " + getPKeySender().hashCode() + "\n" +
                "pKey_recipient = " + getPKeyReceiver().hashCode() + "\n" +
                "pigcoins = " + getPigcoins() + "\n" +
                "message = " + getMessage() + "\n";
    }

}
