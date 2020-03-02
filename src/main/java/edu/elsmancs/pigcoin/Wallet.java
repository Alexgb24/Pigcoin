package edu.elsmancs.pigcoin;

import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Wallet {



    private PrivateKey SKey=null;
    private PublicKey address=null;
    private double totalInput =0d;
    private double totalOutput =0d;
    private double balance=0d;
    private ArrayList<Transaction> inputTransactions=null;
    private ArrayList<Transaction> outputTransactions=null;
    
}