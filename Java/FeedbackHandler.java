import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Klasse: FeedbackHandler
 * ----------------------
 * Diese Klasse implementiert einen SAX-Eventhandler zum Einlesen
 * einzelner Feedback-XML-Dateien.
 *
 * Der Handler liest:
 *  - Elemente (z. B. <vorname>, <nachname>)
 *  - Attribute (z. B. note, note_aussehen)
 *  - Textinhalte (CDATA)
 *
 * Die gelesenen Daten werden in einem FeedbackData-Objekt gespeichert,
 * das nach dem Parsen vom Hauptprogramm (SAXValidator) weiterverarbeitet wird.
 */
public class FeedbackHandler extends DefaultHandler {

    /** Enthält alle Daten eines einzelnen Feedbacks */
    private FeedbackData data = new FeedbackData();

    /**
     * StringBuilder zum Sammeln von Zeicheninhalten.
     * SAX kann characters() mehrfach pro Element aufrufen,
     * daher ist ein Puffer notwendig.
     */
    private StringBuilder content = new StringBuilder();

    /**
     * Gibt das vollständig gefüllte FeedbackData-Objekt zurück.
     */
    public FeedbackData getData() {
        return data;
    }

    /**
     * Start eines XML-Elements:
     * Wird aufgerufen, wenn ein XML-Start-Tag erreicht wird.
     * Hier werden insbesondere Attribute ausgelesen.
     *
     * @param uri Namespace-URI (nicht verwendet)
     * @param localName Lokaler Elementname
     * @param qName Qualifizierter Elementname
     * @param attributes Attribute des Elements
     */
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

    /**
     * Zeicheninhalt eines Elements:
     * Wird aufgerufen, wenn Zeichen innerhalb eines Elements gelesen werden.
     * Der Inhalt wird im StringBuilder gesammelt.
     */
    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        content.append(ch, start, length);
    }

    /**
     * Ende eines XML-Elements:
     * Wird aufgerufen, wenn ein XML-End-Tag erreicht wird.
     * Hier werden die gesammelten Textinhalte ausgewertet.
     */
    @Override
    public void endElement(String uri, String localName, String qName)
            throw SAXException {
        String txt = content.toString().trim();
        switch (qName) {
            /* Persönliche Daten */
            case "anrede"        -> data.anrede = txt;
            case "vorname"       -> data.vorname = txt;
            case "nachname"      -> data.nachname = txt;
            case "alter"           -> data.alter = safeParseInt(txt, 0);

            /* Kontaktdaten */
            case "email"         -> data.email = emptyToNull(txt);
            case "telefon"       -> data.telefon = emptyToNull(txt);
            case "website"       -> data.website = emptyToNull(txt);
            case "password"      -> data.password = txt;

            /* Zusatzinformationen */
            case "aufmerksamkeit"-> data.aufmerksamkeit = txt;
            case "erneuter_besuch"       -> data.erneuter_besuch = safeParseBoolean(txt);
            case "question"      -> data.question = safeParseBoolean(txt);
            case "copy"          -> data.copy = safeParseBoolean(txt);

            /* Freitext */
            case "feedbackText"  -> data.feedbackText = txt;
        }
    }

    /* Fehler vermeiden Methoden */
    /**
     * Parst einen String sicher zu boolean.
     * Erwartet "true" (Groß-/Kleinschreibung egal).
     *
     * @param s Eingabestring
     * @return true bei "true", sonst false
     */
    private static int safeParseInt(String s, int fallback) {
        try {
            return Integer.parseInt(s.trim());
        } catch (Exception e) {
            return fallback;
        }
    }

    /**
     * Wandelt leere Strings in null um.
     *
     * @param s Eingabestring
     * @return null bei leerem String, sonst Originalwert
     */
    private static boolean safeParseBoolean(String s) {
        return "true".equalsIgnoreCase(s.trim());
    }

    /**
     * Wandelt leere Strings in null um.
     *
     * @param s Eingabestring
     * @return null bei leerem String, sonst Originalwert
     */
    private static String emptyToNull(String s) {
        return (s == null || s.isBlank()) ? null : s;
    }
}
