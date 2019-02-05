public class MainActivity {

    public static void main(String[] args) {

        try {
            ParserBFT parserBFT = new ParserBFT();
            parserBFT.parse("stoly");
            parserBFT.parse("stulya");
            parserBFT.parse("mebel_1/komody");
            parserBFT.parse("mebel_1/komplekty");
            parserBFT.parse("mebel_1/kresla");
            parserBFT.parse("mebel_1/krovati");
            parserBFT.parse("mebel_1/prikhozhie");
            parserBFT.parse("mebel_1/pufy");
            parserBFT.parse("mebel_1/stellazhi");
            parserBFT.parse("mebel_1/shkafy");
            ParserZOV parserZOV = new ParserZOV();
            parserZOV.parseKitchens();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}