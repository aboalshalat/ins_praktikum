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

public class SAXValidator {
    static ArrayList<FeedbackData> list = new ArrayList<>();

    public static void main(String[] args) {
        String xmlDir = (args.length > 0) ? args[0] : "../XML/input";
        File folder = new File(xmlDir);

        System.out.println("Arbeitsverzeichnis: " + new File(".").getAbsolutePath());
        System.out.println("XML-Ordner: " + folder.getPath());

        if (!folder.exists() || !folder.isDirectory()) {
            System.out.println("Fehler: XML-Ordner nicht gefunden: " + folder.getPath());
            return;
        }
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".xml"));

        if (files == null || files.length == 0) {
            System.out.println("Keine XML-Dateien gefunden in: " + folder.getPath());
            return;
        }

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

        // Ausgabe
        System.out.println("Anzahl Feedback: " + total);
        System.out.println("Durchschnittliche Note für das Aussehen: " + durchschnittLayout);
        System.out.println("Durchschnittliche Note für das Inhalt: " + durchschnittInhalt);
        System.out.println("Prozent der Besucher, die wiederkommen möchten: %" + prozentVisitor);
        System.out.println("Anzahl der Besucher mit E-Mail-Kopie: " + countCopy);

        // Aufgabe 7.5 XML schreiben
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

                // ===== Besucher =====
                xml.writeStartElement("besucher");
                if (f.anrede != null) xml.writeAttribute("anrede", f.anrede);
                xml.writeAttribute("vorname", f.vorname);
                xml.writeAttribute("nachname", f.nachname);
                xml.writeStartElement("alter");
                xml.writeCharacters(String.valueOf(f.alter));
                xml.writeEndElement();

                // ===== Kontakt =====
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

                // ===== Bewertung =====
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

                // ===== Info =====
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

            // Entwickler
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

        // Aufgabe 7.7 – Automatische Validierung der Ausgabedatei
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

    private static void validateXML(File xmlFile) throws Exception {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        factory.setNamespaceAware(true);

        SAXParser parser = factory.newSAXParser();
        parser.parse(xmlFile, new MyErrorHandler());
        FeedbackHandler handler = new FeedbackHandler();
        parser.parse(xmlFile, handler);
        list.add(handler.getData());
        System.out.println(list.size());
    }

    static String boolToJaNein(boolean b) {
        return b ? "ja" : "nein";
    }

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
