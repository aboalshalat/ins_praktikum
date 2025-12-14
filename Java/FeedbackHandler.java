import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class FeedbackHandler extends DefaultHandler {

    private FeedbackData data = new FeedbackData();
    private StringBuilder content = new StringBuilder();
    public FeedbackData getData() {
        return data;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        content.setLength(0);
        switch (qName) {
            case "inhalt" -> {
                String note = attributes.getValue("note");
                data.noteInhalt = safeParseInt(note, -1);
            }
            case "aussehen" -> {
                String note = attributes.getValue("note_aussehen");
                data.noteAussehen = safeParseInt(note, -1);
            }
            case "zeitpunkt" -> {
                data.datum = attributes.getValue("datum");
                data.uhrzeit = attributes.getValue("uhrzeit");
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        content.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throw SAXException {
        String txt = content.toString().trim();
        switch (qName) {
            case "anrede"        -> data.anrede = txt;
            case "vorname"       -> data.vorname = txt;
            case "nachname"      -> data.nachname = txt;
            case "alter"           -> data.alter = safeParseInt(txt, 0);
            case "email"         -> data.email = emptyToNull(txt);
            case "telefon"       -> data.telefon = emptyToNull(txt);
            case "website"       -> data.website = emptyToNull(txt);
            case "password"      -> data.password = txt;
            case "aufmerksamkeit"-> data.aufmerksamkeit = txt;
            case "erneuter_besuch"       -> data.erneuter_besuch = safeParseBoolean(txt);
            case "question"      -> data.question = safeParseBoolean(txt);
            case "copy"          -> data.copy = safeParseBoolean(txt);
            case "feedbackText"  -> data.feedbackText = txt;
        }
    }

    private static int safeParseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    private static boolean safeParseBoolean(String s) {
        return "true".equalsIgnoreCase(s.trim());
    }

    private static String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
