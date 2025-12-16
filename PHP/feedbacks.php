<?php
/* =========================================================
   Datei: feedbacks.php
   ---------------------------------------------------------
   Diese Datei dient zur Auswertung aller gesammelten
   Feedback-Daten aus der zentralen XML-Datenbankdatei
   (feedbackdatenbank.xml).

   Die XML-Datei wurde zuvor durch das Java-Programm
   (SAXValidator) erzeugt und validiert.
   ========================================================= */

/* Pfad zur zentralen XML-Ausgabedatei */
$xmlFile = "../XML/output/feedbackdatenbank.xml";
if (!file_exists($xmlFile)) {
    die("XML-Datei nicht gefunden.");
}
$xml = simplexml_load_file($xmlFile);
if ($xml === false) {
    die("Fehler beim Laden der XML-Datei.");
}
?>
<!DOCTYPE html>
<html lang="de">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="../CSS/Print.css" media="print">
        <link rel="stylesheet" href="../CSS/Desktop.css">
        <link rel="stylesheet" href="../CSS/Tablet.css">
        <link rel="stylesheet" href="../CSS/Mobile.css">
        <title>Auswertung</title>
    </head>
    <body>
    <?php require_once __DIR__ . "/../HTML/Layout/Navigation.php"; ?>
    <main>
        <h1>Auswertung – alle Feedbacks</h1>
        <table class="cv-table feedback-table">
            <tr class="feedback-title">
                <th colspan="6">Alle Feedbacks</th>
            </tr>
            <tr class="feedback-header">
                <th>Name</th>
                <th>Alter</th>
                <th>Note Inhalt</th>
                <th>Note Aussehen</th>
                <th>Kommentar</th>
                <th>Wiederbesuch</th>
            </tr>
            <?php foreach ($xml->feedback as $f): ?>
                <?php
                $name = $f->besucher['vorname'] . " " . $f->besucher['nachname'];
                $alter = (string)$f->besucher->alter;
                $noteInhalt = (string)$f->bewertung['note_inhalt'];
                $noteAussehen = (int)$f->bewertung['note_aussehen'];
                $feedbackText = isset($f->bewertung->vorschlag)
                    ? (string)$f->bewertung->vorschlag
                    : "";
                $erneuterBesuch = (string)$f->bewertung['erneuter_besuch'];
                $badInhalt = in_array($noteInhalt, ["mangelhaft", "ungenügend"]);
                $badAussehen = ($noteAussehen === 5 || $noteAussehen === 6);
                ?>
                <tr>
                    <td><?= htmlspecialchars($name) ?></td>
                    <td><?= htmlspecialchars($alter) ?></td>
                    <td class="<?= $badInhalt ? 'feedback-alert' : '' ?>">
                        <?= htmlspecialchars($noteInhalt) ?>
                    </td>
                    <td class="<?= $badAussehen ? 'feedback-alert' : '' ?>">
                        <?= htmlspecialchars($noteAussehen) ?>
                    </td>
                    <td><?= htmlspecialchars($feedbackText) ?></td>
                    <td><?= htmlspecialchars($erneuterBesuch) ?></td>
                </tr>
            <?php endforeach; ?>
        </table>
    </main>
    <?php require_once __DIR__ . "/../HTML/Layout/Footer.php"; ?>
    </body>
</html>
