import java.io.File;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.ArrayList;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;
import java.io.FileOutputStream;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * Klasse: SAXValidator
 * -------------------
 * Diese Klasse ist das Hauptprogramm zur Verarbeitung der Feedback-Daten.
 *
 * Aufgaben:
 *  - Einlesen und Validieren einzelner Feedback-XML-Dateien (DTD)
 *  - Sammeln der Daten in FeedbackData-Objekten
 *  - Statistische Auswertung der Feedbacks
 *  - Erzeugen einer gemeinsamen XML-Datenbankdatei
 *  - Pretty-Print der Ausgabedatei
 *  - Erneute Validierung der erzeugten XML-Datei
 *
 * Diese Klasse erfüllt die Anforderungen der Aufgaben 7.1 bis 7.7
 * des INS-Praktikums.
 */
public class SAXValidator {

    /**
     * Zentrale Datensammlung
     * Liste aller eingelesenen Feedback-Datensätze.
     * Jedes Element entspricht einer einzelnen Feedback-XML-Datei.
     */
    static ArrayList<FeedbackData> list = new ArrayList<>();


    /**
     * Hauptmethode des Programms.
     *
     * @param args Optionaler Pfad zum XML-Eingabeordner
     */
    public static void main(String[] args) {
        /* Eingabeordner bestimmen */
        String xmlDir = (args.length > 0) ? args[0] : "../XML/input";
        File folder = new File(xmlDir);

        System.out.println("Arbeitsverzeichnis: " + new File(".").getAbsolutePath());
        System.out.println("XML-Ordner: " + folder.getPath());

        /* Existenzprüfung des XML-Ordners */
        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Fehler: XML-Ordner nicht gefunden: " + folder.getPath());
            return;
        }
        /* Alle XML-Dateien im Ordner sammeln */
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));

        if (files == null || files.length == 0) {
            System.out.println("Keine XML-Dateien gefunden in: " + folder.getPath());
            return;
        }
        /* Einlesen & Validieren der Einzeldateien */
        for (File xmlFile : files) {
            System.out.println("\nValidiere: " + xmlFile.getName());
            try {
                validateXML(xmlFile);
                System.out.println("✅ Datei ist gültig.");
            } catch (Exception e) {
                System.out.println("❌ Fehler gefunden:");
                System.out.println(e.getMessage());
            }
        }
        if (list.isEmpty()) {
            System.out.println("Keine Feedbacken gefunden.");
            return;
        }

        /* Statistische Auswertung */
        int sumNoteAussehen = 0;
        int sumNoteInhalt = 0;
        int countVisitor = 0;
        int countCopy = 0;

        for (FeedbackData f: list) {
            if (f.noteAussehen > 0) sumNoteAussehen += f.noteAussehen;
            if (f.noteInhalt > 0) sumNoteInhalt += f.noteInhalt;
            if (f.erneuter_besuch) countVisitor++;
            if (f.copy) countCopy++;
        }

        int total = list.size();
        double durchschnittLayout = (double) sumNoteAussehen / total;
        double durchschnittInhalt = (double) sumNoteInhalt / total;
        double prozentVisitor =  ((double) countVisitor / total) * 100;

        /* Ausgabe der Statistik */
        System.out.println("Anzahl Feedback: " + total);
        System.out.println("Durchschnittliche Note für das Aussehen: " + durchschnittLayout);
        System.out.println("Durchschnittliche Note für das Inhalt: " + durchschnittInhalt);
        System.out.println("Prozent der Besucher, die wiederkommen möchten: %" + prozentVisitor);
        System.out.println("Anzahl der Besucher mit E-Mail-Kopie: " + countCopy);

        // Aufgabe 7.5 – Gemeinsame XML-Datenbank erzeugen
        try {
            String outputFile = "../XML/output/feedbackdatenbank.xml";

            XMLOutputFactory factory = XMLOutputFactory.newInstance();
            XMLStreamWriter xml = factory.createXMLStreamWriter(
                    new FileOutputStream(outputFile), "UTF-8"
            );

            xml.writeStartDocument("UTF-8", "1.0");
            xml.writeDTD("<!DOCTYPE feedbackdatenbank SYSTEM \"../../DTD/feedbackdatenbank.dtd\">");
            xml.writeStartElement("feedbackdatenbank");

            for (FeedbackData f : list) {
                xml.writeStartElement("feedback");

                /* ===== Besucher ===== */
                xml.writeStartElement("besucher");
                if (f.anrede != null) xml.writeAttribute("anrede", f.anrede);
                xml.writeAttribute("vorname", f.vorname);
                xml.writeAttribute("nachname", f.nachname);
                xml.writeStartElement("alter");
                xml.writeCharacters(String.valueOf(f.alter));
                xml.writeEndElement();

                /* ===== Kontakt ===== */
                xml.writeStartElement("kontakt");
                xml.writeAttribute("rueckfrage_erlaubt", boolToJaNein(f.question));
                if (f.email != null) {
                    xml.writeStartElement("emailadresse");
                    xml.writeCharacters(f.email);
                    xml.writeEndElement();
                }
                if (f.telefon != null) {
                    xml.writeStartElement("telefonnummer");
                    xml.writeCharacters(f.telefon);
                    xml.writeEndElement();
                }
                if (f.website != null) {
                    xml.writeStartElement("website");
                    xml.writeCharacters(f.website);
                    xml.writeEndElement();
                }
                xml.writeEndElement(); // kontakt
                xml.writeEndElement(); // besucher

                /* ===== Bewertung ===== */
                xml.writeStartElement("bewertung");
                xml.writeAttribute("erneuter_besuch", boolToJaNein(f.erneuter_besuch));
                xml.writeAttribute("note_inhalt", mapNoteInhalt(f.noteInhalt));
                xml.writeAttribute("note_aussehen", String.valueOf(f.noteAussehen));
                if (f.feedbackText != null && !f.feedbackText.isBlank()) {
                    xml.writeStartElement("vorschlag");
                    xml.writeCharacters(f.feedbackText);
                    xml.writeEndElement();
                }
                xml.writeEndElement(); // bewertung

                /* ===== Info ===== */
                xml.writeStartElement("info");
                xml.writeStartElement("email-gesendet");
                xml.writeCharacters(boolToJaNein(f.copy));
                xml.writeEndElement();
                xml.writeStartElement("datum");
                xml.writeCharacters(f.datum);
                xml.writeEndElement();
                xml.writeStartElement("uhrzeit");
                xml.writeCharacters(f.uhrzeit);
                xml.writeEndElement();
                xml.writeEndElement(); // info
                xml.writeEndElement(); // feedback
            }

            /* Entwicklerinformation */
            xml.writeStartElement("entwickler_parser");
            xml.writeCharacters("Amer");
            xml.writeEndElement();

            xml.writeEndElement(); // feedbackdatenbank
            xml.writeEndDocument();
            xml.close();

            System.out.println("Gemeinsame XML-Datei erstellt: " + outputFile);
            prettyPrintXML(outputFile);
            System.out.println("XML wurde formatiert (pretty print).");
        } catch (Exception e) {
            e.printStackTrace();
        }

        /* Aufgabe 7.7 – Validierung der Ausgabedatei */
        System.out.println("\nStarte automatische Validierung der Ausgabedatei...");

        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setValidating(true);
            factory.setNamespaceAware(true);

            SAXParser parser = factory.newSAXParser();
            parser.parse(
                    new File("../XML/output/feedbackdatenbank.xml"),
                    new MyErrorHandler()
            );
            System.out.println("✅ Ausgabedatei ist gültig (DTD-konform).");
        } catch (Exception e) {
            System.out.println("❌ Fehler bei der Validierung der Ausgabedatei:");
            System.out.println(e.getMessage());
        }
    }


    /**
     * Formatiert eine XML-Datei lesbar (Pretty Print).
     *
     * @param filePath Pfad zur XML-Datei
     */
    static void prettyPrintXML(String filePath) throws Exception {
        Document doc = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(new File(filePath));
        Transformer transformer = TransformerFactory
                .newInstance()
                .newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(
                "{http://xml.apache.org/xslt}indent-amount", "4"
        );
        transformer.setOutputProperty(
                OutputKeys.DOCTYPE_SYSTEM,
                "../../DTD/feedbackdatenbank.dtd"
        );
        transformer.transform(
                new DOMSource(doc),
                new StreamResult(new File(filePath))
        );
    }


    /**
     * Validiert eine einzelne Feedback-XML-Datei
     * und liest deren Inhalte in ein FeedbackData-Objekt ein.
     *
     * @param xmlFile XML-Datei
     */
    private static void validateXML(File xmlFile) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);

        SAXParser parser = factory.newSAXParser();

        /* 1. DTD-Validierung */
        parser.parse(xmlFile, new MyErrorHandler());

        /* 2. Einlesen der Daten */
        FeedbackHandler handler = new FeedbackHandler();
        parser.parse(xmlFile, handler);

        /* 3. Speicherung */
        //list.add(handler.getData());
    }

    /**
     * Wandelt boolean in "ja" / "nein" um.
     */
    static String boolToJaNein(boolean b) {
        return b ? "ja" : "nein";
    }

    /**
     * Wandelt numerische Noten in Textbewertungen um.
     */
    static String mapNoteInhalt(int n) {
        return switch (n) {
            case 1 -> "sehr_gut";
            case 2 -> "gut";
            case 3 -> "befriedigend";
            case 4 -> "ausreichend";
            case 5 -> "mangelhaft";
            case 6 -> "ungenuegend";
            default -> "befriedigend";
        };
    }

    /**
     * Eigener ErrorHandler für verständliche SAX-Fehlermeldungen.
     */
    static class MyErrorHandler extends DefaultHandler {
        @Override
        public void warning(SAXParseException e) throws SAXException {
            System.out.println("⚠️ Warnung (Zeile " + e.getLineNumber() + "): " + e.getMessage());
        }
        @Override
        public void error(SAXParseException e) throws SAXException {
            throw new SAXException("Fehler (Zeile " + e.getLineNumber() + "): " + e.getMessage());
        }
        @Override
        public void fatalError(SAXParseException e) throws SAXException {
            throw new SAXException("Schwerer Fehler (Zeile " + e.getLineNumber() + "): " + e.getMessage());
        }
    }
}
