public class test {
    public static void main(String[] arg){
        BigTwo bt = new BigTwo();
        Deck d = new BigTwoDeck();
        d.initialize();
        d.shuffle();
        bt.start(d);

    }
}
