public class Main {
    public static void main(String[] args) {
        Konsument konsument = new Konsument(2);
        konsument.init();
        konsument.consumeTopicByEveryone();
    }
}
