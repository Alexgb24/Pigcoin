package edu.elsmancs.pigcoin;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Wallet {

    private PublicKey address = null;
    private PrivateKey sKey = null;
    private double total_input = 0d;
    private double total_output = 0d;
    private double balance = 0d;
    private List<Transaction> inputTransactions = null;
    private List<Transaction> outputTransactions = null;

    public Wallet() {
    };

    public void setSK(PrivateKey sKey) {
        this.sKey = sKey;
    }

    public PrivateKey getSKey() {
        return this.sKey;
    }

    public void setAddress(PublicKey pKey) {
        this.address = pKey;
    }

    public PublicKey getAddress() {
        return this.address;
    }

    public void setTotalInput(double total_input) {
        this.total_input = total_input;
    }


    public void setTotalOutput(double total_output) {
        this.total_output = total_output;
    }
    
    public void setOutputTransactions(List<Transaction> outputTransactions) {
        this.outputTransactions = outputTransactions;
    }

    public void setInputTransactions(List<Transaction> transactions) {
		this.inputTransactions = transactions;
	}
    
    public List<Transaction> getInputTransactions() {
		return this.inputTransactions;
	}
    
    public List<Transaction> getOutputTransactions() {
        return this.outputTransactions;
    }
    
    public double getTotalOutput() {
        return this.total_output;
    }

    public double getBalance() {
		return this.balance;
    }
    
    public double getTotalInput() {
        return this.total_input;
    }
    
    public void generateKeyPair() {
        KeyPair pair = GenSig.generateKeyPair();
        this.setSK(pair.getPrivate());
        this.setAddress(pair.getPublic());
    }

    public void updateBalance() {
		this.balance = this.getTotalInput() - this.getTotalOutput();
    }

    public void loadCoins(BlockChain bChain) {
        double[] pigcoins = {0d, 0d};
        pigcoins = bChain.loadWallet(getAddress());
        setTotalInput(pigcoins[0]);
        setTotalOutput(pigcoins[1]);
        updateBalance();
    }

    public void loadInputTransactions(BlockChain bChain) {
        setInputTransactions(bChain.loadInputTransactions(getAddress()));        
    }
    
    public void loadOutputTransactions(BlockChain bChain) {
        setOutputTransactions(bChain.loadOutputTransactions(getAddress()));        
    }
    
    public Map<String, Double> collectCoins(double pigcoins) {
        
        Map<String, Double> collectedCoins = new LinkedHashMap<>();

        if (getInputTransactions() == null) {
            return null;
        }

        if (pigcoins > getBalance()) {
            return null;
        }

        Double achievedCoins = 0d;

        Set<String> consumedCoins = new HashSet<>();
        if (getOutputTransactions() != null) {
            for (Transaction transaction : getOutputTransactions()) {
                consumedCoins.add(transaction.getPrevHash());
            }   
        }             

        for (Transaction transaction : getInputTransactions()) {

            if (consumedCoins.contains(transaction.getHash())) {
                continue;
            }

            if (transaction.getPigCoins() == pigcoins) {
                collectedCoins.put(transaction.getHash(), transaction.getPigCoins());
                consumedCoins.add(transaction.getHash());
                break;
            } else if (transaction.getPigCoins() > pigcoins) {
                collectedCoins.put(transaction.getHash(), pigcoins);
                collectedCoins.put("CA_" + transaction.getHash(), transaction.getPigCoins() - pigcoins);
                consumedCoins.add(transaction.getHash());
                break;
            } else {
                collectedCoins.put(transaction.getHash(), transaction.getPigCoins());
                achievedCoins = transaction.getPigCoins();
                pigcoins = pigcoins - achievedCoins;
                consumedCoins.add(transaction.getHash());
            }

        }
        // getInputTransactions().removeAll(consumedCoins);
        return collectedCoins;
    }

    public byte[] signTransaction(String message) {
        return GenSig.sign(getSKey(), message);
    }

    public void sendCoins(PublicKey pKey_recipient, Double coins, String message, BlockChain bChain) {

        Map<String, Double> consumedCoins = new LinkedHashMap<>();
        
        consumedCoins = collectCoins(coins);

        if (consumedCoins != null) {
            bChain.processTransactions(getAddress(), pKey_recipient, consumedCoins, message, signTransaction(message));
        }
        
        this.loadCoins(bChain);
    }

    @Override
    public String toString() {
        return "\n" + "Wallet = " + getAddress().hashCode() + "\n" + 
                      "Total input = " + getTotalInput() + "\n" +
                      "Total output = " + getTotalOutput() + "\n" +
                      "Balance = " + getBalance() + "\n";
    }

}