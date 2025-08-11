import java.util.Arrays;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import javax.crypto.Cipher;
import java.security.*;
import java.util.Base64;

/**
 *  This is a Blockchain structure with the ability to create blocks and chain them through hashes.
 *  This has been created with further implementation in mind (for validation).
 *
 *  These are future improvements I would make:
 *      1. implement the validation processes (miners for PoW, voting for PoS, Authorities for PoA) and It has been created with this in mind
 *      2. implement a checking algorithm to reject/remove invalid blocks
 *      3. add a space for blocks to go for validation and make it so blocks can be made unverified
 */

public class Blockchain {
    
    private ArrayList<Block> blockChain = new ArrayList<>();
    private String outputData = "\nThis is the blockchain created:\n\n";
    private BigInteger targetHash;
    //----------------------------------------------------------------------------------------------------------------
    public Blockchain(long difficultyTarget) {
        
        this.targetHash = generateTargetHash(difficultyTarget);
    }
    //----------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        
        Blockchain blockchain = new Blockchain(0x0d00ffffL); // Example difficulty target in compact format

        //Starts Blockchain with an empty Block
        Block genisis = new Block(0, TransactionalData.Genisis, null, (long) 0);

        //1st Block
        Block one = new Block(genisis.getMyhash(), TransactionalData.transactions1, TransactionalData.signatures1, 52);

        //2nd Block
        String[] contents = new String[]{"96584076" + Arrays.hashCode(TransactionalData.transactions2)};
        String signature;
        try {
            signature = TransactionalData.Authority1.encrypt(Arrays.toString(contents));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Block two = new Block(one.getMyhash(), TransactionalData.transactions2, TransactionalData.signatures2, signature);


        Block[] Blocks = new Block[]{genisis, one, two};
        blockchain.addBlock(Blocks);

        System.out.println(blockchain.outputData + blockchain.iterateThroughBlocks());
        System.out.println("Target Hash: " + blockchain.targetHash);


    }
    //----------------------------------------------------------------------------------------------------------------
    private static BigInteger generateTargetHash(long DT) {
        /**
         * generate a hash to be solved by a miner
         */
        int exponent = (int) (DT >> 24);
        int mantissa = (int) (DT & 0x007fffffL);
        if ((DT & 0x00800000L) != 0) {
            mantissa |= 0xff800000;
        }
        BigInteger target = BigInteger.valueOf(mantissa).shiftLeft(8 * (exponent - 3));
        return target;
    }
    //----------------------------------------------------------------------------------------------------------------
    public void addBlock(Block[] Block) {
        for (int i = 0; i < Block.length; i++) {
            blockChain.add(Block[i]);
        }
    }
    //----------------------------------------------------------------------------------------------------------------
    public void addBlock(Block Block) {
        blockChain.add(Block);
    }
    //----------------------------------------------------------------------------------------------------------------
    public String iterateThroughBlocks() {
            String returnStatement = "";
            for (int i = 0; i < blockChain.size(); i++) {
                returnStatement += blockChain.get(i).toString() + " \n";
            }
            return returnStatement;
        }
    //----------------------------------------------------------------------------------------------------------------
    public Boolean validateBlock(Block Block) {
        Boolean returnStatement = false;
            switch(Block.getTypeOfBlock()) {
                case PoW -> if(){returnStatement == true} nonce;                                         // IMPLEMENT ME
                case PoS -> if(votesOutOf100 > 50){returnStatement == true} votesOutOf100;               // Make me more secure
                case PoA -> if(){returnStatement == true} authoritySignature;                            // IMPLEMENT ME
            }
        return returnStatement;
        }
    //----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return iterateThroughBlocks();
    }
    //---------------------------------------------------------------------------------------------------------------
}
//--------------------------------------------------------------------------------------------------------------------
class TransactionalData {
    
    static Encryption_Signature User1 = new Encryption_Signature("Apollo");
    static Encryption_Signature User2 = new Encryption_Signature("Ares");
    static Encryption_Signature User3 = new Encryption_Signature("Dionysus");
    static Encryption_Signature User4 = new Encryption_Signature("Hephaestus");
    static String[] Genisis = {"Apollo is given 20 coins", "Ares is given 20 coins", "Dionysus is given 20 coins", "Hephaestus is given 20 coins"};
    static String[] transactions1 = {"Apollo gives 20 coins to Ares", "Ares gives 20 coins to Apollo"};
    static String[] transactions2 = {"Dionysus gives 20 coins to Apollo", "Apollo gives 40 coins to Hephaestus"};

    static String[] signatures1;
    static String[] signatures2;
    static {
        try {
            signatures1 = new String[]{User1.encrypt(transactions1[0]), User2.encrypt(transactions1[1])};
            signatures2 = new String[]{User3.encrypt(transactions2[0]), User1.encrypt(transactions2[1])};
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    static Encryption_Signature[] accounts1 = {User1, User2, User3, User4};

    //PoA
    static Encryption_Signature Authority1 = new Encryption_Signature("Momus");
    static Encryption_Signature Authority2 = new Encryption_Signature("Moros");
    static Encryption_Signature[] Authorities = {Authority1, Authority2};

}

public class Block {
    
    private int previoushash;
    private String[] transactions;
    private String[] signatures;
    private Object[] contents;
    private int myhash;

    private long nonce;
    private int votesOutOf100;
    private String authoritySignature;

    private TypeOfBlock typeOfBlock;
    //----------------------------------------------------------------------------------------------------------------
    //PoW
    public Block(int previoushash, String[] transactions, String[] signatures, long nonce) {
        
        this.previoushash = previoushash;
        this.transactions = transactions;
        this.signatures = signatures;

        this.typeOfBlock = TypeOfBlock.PoW;
        this.nonce = nonce;

        this.contents = new Object[]{Arrays.hashCode(transactions), previoushash, nonce};
        this.myhash = Arrays.hashCode(contents);
    }
    //----------------------------------------------------------------------------------------------------------------
    //PoS
    public Block(int previoushash, String[] transactions, String[] signatures, int votesOutOf100) {
        
        this.previoushash = previoushash;
        this.transactions = transactions;
        this.signatures = signatures;

        this.typeOfBlock = TypeOfBlock.PoS;
        this.votesOutOf100 = votesOutOf100;

        this.contents = new Object[]{Arrays.hashCode(transactions), previoushash};
        this.myhash = Arrays.hashCode(contents);
    }
    //----------------------------------------------------------------------------------------------------------------
    //PoA
    public Block(int previoushash, String[] transactions, String[] signatures, String authoritySignature) {
        
        this.previoushash = previoushash;
        this.transactions = transactions;
        this.signatures = signatures;

        this.typeOfBlock = TypeOfBlock.PoA;
        this.authoritySignature = authoritySignature;

        this.contents = new Object[]{Arrays.hashCode(transactions), previoushash};
        this.myhash = Arrays.hashCode(contents);
    }
    //----------------------------------------------------------------------------------------------------------------
    public int getMyhash() {
        return myhash;
    }
    //----------------------------------------------------------------------------------------------------------------
    public int getPrevioushash() {
        return previoushash;
    }
    //----------------------------------------------------------------------------------------------------------------
    public String[] getTransactions() {
        return transactions;
    }
    //----------------------------------------------------------------------------------------------------------------
    public TypeOfBlock getTypeOfBlock() {
        return typeOfBlock;
    }
    //----------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        
        String returnStatement = "This is a " + typeOfBlock + " with " + transactions.length + " transactions." +
                "\n Previous Hash: " + previoushash +
                "\n My Hash: " + myhash;
        switch(typeOfBlock) {
            case PoW -> returnStatement += "\n Proof of Validity: " + nonce;
            case PoS -> returnStatement += "\n Proof of Validity: " + votesOutOf100;
            case PoA -> returnStatement += "\n Proof of Validity: " + authoritySignature;
        }
        return returnStatement + "\n";
    }
    //----------------------------------------------------------------------------------------------------------------
}

public class Encryption_Signature {
    
    private PrivateKey privateKey;
    public PublicKey publicKey;
    public String name;
    //----------------------------------------------------------------------------------------------------------------
    public Encryption_Signature(String name) {
        initializer();
        this.name = name;
    }
    //----------------------------------------------------------------------------------------------------------------
    public void initializer() {
        
        try {
            KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
            generator.initialize(1024);
            KeyPair pair = generator.generateKeyPair();
            this.privateKey = pair.getPrivate();
            this.publicKey = pair.getPublic();
        }
        catch (Exception ignored) {
        }
    }
    //----------------------------------------------------------------------------------------------------------------
    private static String encode(byte[] data) {return Base64.getEncoder().encodeToString(data);}
    //----------------------------------------------------------------------------------------------------------------
    private static byte[] decode(String data) {return Base64.getDecoder().decode(data);}
    //----------------------------------------------------------------------------------------------------------------
    public String encrypt(String message) throws Exception {
        
        byte[] messageToBytes = message.getBytes();
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] encryptedBytes = cipher.doFinal(messageToBytes);
        return encode(encryptedBytes);
    }
    //----------------------------------------------------------------------------------------------------------------
    public String decrypt(String encryptedMessage) throws Exception {
        
        byte[] encryptedBytes = decode(encryptedMessage);
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] decryptedMessage = cipher.doFinal(encryptedBytes);
        return new String(decryptedMessage, "UTF8");
    }
    //----------------------------------------------------------------------------------------------------------------
}

public enum TypeOfBlock {
    PoW, PoS, PoA
}

