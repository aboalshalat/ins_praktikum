<?php

    $anrede      = $_POST['anrede'] ?? "";
    $vorname     = $_POST['firstname'] ?? "";
    $nachname    = $_POST['lastname'] ?? "";
    $email       = $_POST['email'] ?? "";
    $telefon     = $_POST['telefon'] ?? "";
    $alter       = $_POST['alter'] ?? "";
    $website     = $_POST['website'] ?? "";
    $feedback    = $_POST['feedback'] ?? "";
    $aufmerksam  = $_POST['commercial'] ?? "";
    $note        = $_POST['note'] ?? "";
    $note_aussehen = $_POST['note_aussehen'] ?? "";
    $passwort    = $_POST['spamschutz'] ?? "";

    $copy = isset($_POST['feedbackCopy']);
    $erneuter_besuch = isset($_POST['erneuter_besuch']);
    $question = isset($_POST['question']);
    ?>
<!DOCTYPE html>
<html lang="de">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="../CSS/Print.css" media="print">
        <link rel="stylesheet" type="text/css" href="../CSS/Desktop.css">
        <link rel="stylesheet" type="text/css" href="../CSS/Mobile.css">
        <link rel="stylesheet" type="text/css" href="../CSS/Tablet.css">
        <title>Feedback-Auswertung</title>
    </head>
    <body>
        <?php require_once __DIR__ . "/../HTML/Layout/Navigation.php"; ?>
        <main>
            <h1>Feedback-Auswertung</h1>
            <?php
            // Aufgabe 5.1
            echo "<h2>Übergebene Formularwerte</h2><ul>";
            echo "<p>Die Anrede war: <strong>$anrede</strong></p>";
            echo "<p>Der Vorname war: <strong>$vorname</strong></p>";
            echo "<p>Der Nachname war: <strong>$nachname</strong></p>";
            echo "<p>Das Telefon war: <strong>$telefon</strong></p>";
            if ($copy) {
                echo "<p>Die Checkbox „Kopie an meine Mailbox senden“ war: <strong>ausgewählt</strong></p>";
            } else {
                echo "<p>Die Checkbox „Kopie an meine Mailbox senden“ war: <strong>nicht ausgewählt</strong></p>";
            }
            echo "<p>Die Aufmerksamkeit auf die Webseite wurde hervorgerufen durch: 
      <strong>$aufmerksam</strong></p>";
            echo "<p>Note für Inhalt: <strong>$note</strong></p>";
            echo "<p>Note für Aussehen: <strong>$note_aussehen</strong></p>";
            echo "<p>Passwort: <strong>$passwort</strong></p>";
            if ($erneuter_besuch)  echo "<p>Sie möchten die Webseite wieder besuchen.</p>";
            if ($question) echo "<p>Sie stehen für Rückfragen zur Verfügung.</p>";
            echo "<p>Sie sind auf die Webseite aufmerksam geworden durch: <strong>$aufmerksam</strong></p>";
            echo "</ul><hr>";

            //Aufgabe 5.2
            echo "<p>
            Vielen Dank $anrede $vorname $nachname, dass Sie meine Webseite besucht haben.
          </p>";

            //Aufgabe 5.3
            if ($passwort !== "Internetsprachen") {
                echo "<p style='color:red;font-weight:bold;'>
                    Falsches Passwort – Keine weitere Auswertung!
                  </p>";
                exit;
            }

            //Aufgabe 5.4
            if ($copy) {
                if (empty($email)) {
                    echo "<p style='color:orange;font-weight:bold;'>
                    Sie wollten eine Kopie erhalten, haben aber keine E-Mail-Adresse angegeben.
                  </p>";
                } else {
                    echo "<p>Eine Kopie der Nachricht wird an <strong>$email</strong> gesendet.</p>";
                }
            }

            // Aufgabe 5.5
            if ($question) {
                $hatKontakt =
                    !empty($telefon) ||
                    !empty($website) ||
                    !empty($email);
                if (!$hatKontakt) {
                    echo "<p style='color:orange; font-weight:bold;'>
                    Sie stehen für Rückfragen zur Verfügung, 
                    haben jedoch keine Kontaktmöglichkeit angegeben.
                  </p>";
                }
            }

            // Aufgabe 5.9
            if($alter < 18 || $alter >99) {
                echo "<p style='color:red;font-weight:bold;'>
                Das angegebene Alter ($alter) liegt außerhalb des zulässigen Bereichs.</p>";
            }

            // Aufgabe 5.6
            echo "<p>Note für Inhalt: <strong>$note</strong></p>";
            if ($note==1 || $note == 2) {
                echo "<p style='color:Green;font-weight:bold;'> 
                    Ich freue mich das Ihnen der Inhalt der Seite gefallen hat</p>";
            } if ($note==3) {
                echo "<p style='color:yellowgreen;font-weight:bold;'>
                     Ich werde mich bemühen in Zukunft den Inhalt der Seite zu optimieren.</p>";
            }

            // Aufgabe 5.7
            echo "<p>Note für Aussehen: <strong>$note_aussehen</strong></p>";
            switch ($note_aussehen) {
                case "1":
                    echo "<p>Vielen Dank! Es freut mich sehr, 
                        dass Ihnen das Aussehen der Seite besonders gut gefallen hat.</p>";
                    break;
                case "2":
                    echo "<p>Schön zu hören, dass Ihnen das Aussehen gut gefallen hat!</p>";
                    break;
                case "3":
                    echo "<p>Danke! Das Aussehen wurde als befriedigend bewertet – 
                        ich werde versuchen, es weiter zu verbessern.</p>";
                    break;
                case "4":
                    echo "<p>Danke für Ihr ehrliches Feedback. 
                        Ich werde mich bemühen, das Aussehen weiter zu optimieren.</p>";
                    break;
                case "5":
                    echo "<p>Danke! Ich sehe, dass das Aussehen nicht ganz überzeugt hat. 
                        Verbesserung folgt!</p>";
                    break;
                case "6":
                    echo "<p>Danke für die Rückmeldung. 
                        Das Aussehen wurde als ungenügend bewertet – 
                        ich werde unbedingt nachbessern.</p>";
                    break;
                default:
                    echo "<p>Keine gültige Bewertung für das Aussehen angegeben.</p>";
            }

            echo "<p><strong>Ihr Feedback:</strong><br>" . nl2br(htmlspecialchars($feedback)) . "</p>";

            // Aufgabe 5.8
            $date = date("d.m.Y");
            $time = date("H:i");
            echo "<p>Ihre Feedbackwerte wurden am $date um $time Uhr angenommen.</p>";

            $folder = realpath(__DIR__ . '/../XML/input');
            $file = $folder . "/feedback_" . date("Ymd-His") . ".xml";

            $xml = new XMLWriter();
            $xml->openUri($file);
            $xml->setIndent(true);
            $xml->startDocument("1.0", "UTF-8");
            $xml->writeDTD('feedback', null, '../../DTD/feedback.dtd');

            $xml->startElement("feedback");

            $xml->startElement("person");
            $xml->writeElement("anrede", $anrede);
            $xml->writeElement("vorname", $vorname);
            $xml->writeElement("nachname", $nachname);
            $xml->writeElement("alter", $alter);
            $xml->writeElement("email", $email);
            $xml->writeElement("telefon", $telefon);
            $xml->writeElement("website", $website);
            $xml->writeElement("password", $passwort);
            $xml->endElement();

            $xml->startElement("bewertung");
            $xml->startElement("inhalt");
            $xml->writeAttribute("note", $note);
            $xml->endElement();
            $xml->startElement("aussehen");
            $xml->writeAttribute("note_aussehen", $note_aussehen);
            $xml->endElement();
            $xml->endElement();

            $xml->startElement("informationen");
            $xml->writeElement("aufmerksamkeit", $aufmerksam);
            $xml->writeElement("erneuter_besuch", $erneuter_besuch ? "true" : "false");
            $xml->writeElement("question", $question ? "true" : "false");
            $xml->writeElement("copy", $copy ? "true" : "false");
            $xml->endElement();

            $xml->writeElement("feedbackText", $feedback);

            $xml->startElement("zeitpunkt");
            $xml->writeAttribute("datum", date("d.m.Y"));
            $xml->writeAttribute("uhrzeit", date("H:i"));
            $xml->endElement();

            $xml->endElement(); // feedback
            $xml->endDocument();
            $xml->flush();

            echo "<p style='color:green;font-weight:bold;'>Die XML-Datei wurde erfolgreich erstellt.</p>";
            ?>

        </main>
        <?php require_once __DIR__ . "/../HTML/Layout/Footer.php"; ?>
    </body>
</html>
