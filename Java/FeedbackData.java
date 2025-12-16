/**
 * Klasse: FeedbackData
 * --------------------
 * Diese Klasse dient als reiner Datencontainer (DTO – Data Transfer Object)
 * für ein einzelnes Feedback.
 *
 * Sie speichert alle Informationen, die aus einer Feedback-XML-Datei
 * mit einem SAX-Parser eingelesen werden.
 *
 * Die Klasse enthält bewusst keine Logik, sondern nur öffentliche Attribute,
 * damit der SAX-Handler die Daten einfach setzen kann.
 */
public class FeedbackData {
    public String anrede;
    public String vorname;
    public String nachname;
    public int alter;

    public String email;
    public String telefon;
    public String website;
    public String password;

    public String aufmerksamkeit;
    public int noteInhalt;
    public int noteAussehen;

    public boolean erneuter_besuch;
    public boolean question;
    public boolean copy;

    public String feedbackText;

    public String datum;
    public String uhrzeit;
}
