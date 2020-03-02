package edu.elsmancs.pigcoin;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;

public class BlockChain {


    private List<Transaction> blockchain = new ArrayList<Transaction>();

    public BlockChain() {}


    public List<Transaction> getBlockChain() {
        return this.blockchain;
    }

    public void addOrigin(Transaction transaction) {
        this.getBlockChain().add(transaction);
    }

    public void summarize() {

        for (Transaction transaction : this.getBlockChain()) {
            System.out.println(transaction);
        }
    }

    public void summarize(int position) {
        System.out.println(getBlockChain().get(position).toString());
    }

    public double[] loadWallet(PublicKey address) {

        double pigcoinsIn = 0d;
        double pigcoinsOut = 0d;

        for (Transaction transaction : getBlockChain()) {

            if (address.equals(transaction.getPKeyReceiver())) {
                pigcoinsIn += transaction.getPigcoins();
            }
            if (address.equals(transaction.getPKeySender())) {
                pigcoinsOut += transaction.getPigcoins();
            }
        }

        double[] pigcoins = { pigcoinsIn, pigcoinsOut };
        return pigcoins;
    }

    public boolean isSignatureValid(PublicKey pKey, String message, byte[] signedTransaction) {
        return GenSig.verify(pKey, message, signedTransaction);
    }

    public List<Transaction> loadInputTransactions(PublicKey address) {

        List<Transaction> inputTransactions = new ArrayList<Transaction>();

        for (Transaction transaction : getBlockChain()) {
            if (address.equals(transaction.getPKeyReceiver())) {
                inputTransactions.add(transaction);
            }
        }

        return inputTransactions;

    }

    public List<Transaction> loadOutputTransactions(PublicKey address) {

        List<Transaction> outputTransactions = new ArrayList<Transaction>();

        for (Transaction transaction : getBlockChain()) {
            if (address.equals(transaction.getPKeySender())) {
                outputTransactions.add(transaction);
            }
        }

        return outputTransactions;

    }
}