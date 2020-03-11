package edu.elsmancs.pigcoin;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

            if (address.equals(transaction.get_PK_Recipient())) {
                pigcoinsIn += transaction.getPigCoins();
            }
            if (address.equals(transaction.getPKeySender())) {
                pigcoinsOut += transaction.getPigCoins();
            }
        }

        double[] pigcoins = { pigcoinsIn, pigcoinsOut };
        return pigcoins;
    }

    public boolean isSignatureValid(PublicKey pKey, String message, byte[] signedTransaction) {
        return GenSig.verify(pKey, message, signedTransaction);
    }
    
    public boolean isConsumedCoinValid(Map<String, Double> consumedCoins) {
        for (String hash : consumedCoins.keySet()) {
            for (Transaction transaction : blockchain) {
                if (hash.equals(transaction.getPrevHash())) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Transaction> loadInputTransactions(PublicKey address) {
    	   
        List<Transaction> inputTransactions = getBlockChain().stream()
            .filter(transaction -> transaction.get_PK_Recipient().equals(address))
            .collect(Collectors.toCollection(ArrayList<Transaction>::new));
        
        return inputTransactions;
    }

    public List<Transaction> loadOutputTransactions(PublicKey address) {
   
        List<Transaction> outputTransactions = getBlockChain().stream()
            .filter(transaction -> transaction.getPKeySender().equals(address))
            .collect(Collectors.toCollection(ArrayList<Transaction>::new));
        
        return outputTransactions;
    }
    
    public void createTransaction(PublicKey pKey_sender, PublicKey pKey_recipient, Map<String, Double> consumedCoins,
            String message, byte[] signedTransaction) {

        PublicKey addressReceiver = pKey_recipient;
        Integer lastBlock = 0;
        
        for (String prev_hash : consumedCoins.keySet()) {
        
            if (prev_hash.startsWith("CA_")) {
                pKey_recipient = pKey_sender;
            }
            
            lastBlock = blockchain.size() + 1;
            Transaction transaction = new Transaction("hash_" + lastBlock.toString(), prev_hash, pKey_sender,
                    pKey_recipient, consumedCoins.get(prev_hash), message);
            getBlockChain().add(transaction);
            
            pKey_recipient = addressReceiver;
        }
    }

    public void processTransactions(PublicKey pKey_sender, PublicKey pKey_recipient, Map<String, Double> consumedCoins,
            String message, byte[] signedTransaction) {
        
        if (isSignatureValid(pKey_sender, message, signedTransaction) && isConsumedCoinValid(consumedCoins)) {
            createTransaction(pKey_sender, pKey_recipient, consumedCoins, message, signedTransaction);
        }

    }
}