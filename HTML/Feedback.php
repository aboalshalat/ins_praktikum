<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="../CSS/Print.css" media="print">
        <link rel="stylesheet" type="text/css" href="../CSS/Desktop.css">
        <link rel="stylesheet" type="text/css" href="../CSS/Mobile.css">
        <link rel="stylesheet" type="text/css" href="../CSS/Tablet.css">
        <title>Feedback</title>
    </head>
    <body>
        <?php include __DIR__ . "/Layout/Navigation.php"; ?>
        <main>
            <h1>Feedback</h1>
            <form action="../PHP/Feedback.php" method="post">
                <fieldset>
                    <legend>Personal Information</legend>
                    <br>
                    <div>
                        <label for="anrede">Anrede:</label>
                        <select id="anrede" name="anrede">
                            <option>Herr</option>
                            <option>Frau</option>
                            <option>Ing.</option>
                            <option>Dr.</option>
                            <option>Prof.</option>
                        </select>
                        <br><br>
                        <label for="firstname">Vorname</label>
                        <input type="text" name="firstname" id="firstname" required autofocus autocomplete="given-name">
                        <br><br>
                        <label for="lastname">Name</label>
                        <input type="text" name="lastname" id="lastname" required autocomplete="family-name">
                        <br><br>
                        <label for="email">E-Mail</label>
                        <input type="email" name="email" id="email" placeholder="Email adresse eingeben">
                        <br><br>
                        <input id="feedbackCopy" name="feedbackCopy" type="checkbox" value="feedbackCopy">
                        <label for="feedbackCopy">Kopie an meine Mailbox senden.</label>
                        <br><br>
                        <input id="question" name="question" type="checkbox" value="question">
                        <label for="question">Ich stehe für evil. Rückfragen zur Verfügung.</label>
                        <br><br>
                        <label for="telefon">Telefonnummer</label>
                        <input type="tel" name="telefon" id="telefon">
                        <br><br>
                        <label for="website">URL/Website</label>
                        <input type="url" name="website" id="website">
                        <br><br>
                        <label for="alter">Alt</label>
                        <input type="number" name="alter" id="alter" min="18" max="99" value="21">
                        <br><br>
                        <label for="erneuter_besuch">Ich werde die seite wieder besuchen</label>
                        <input type="checkbox" name="erneuter_besuch" id="erneuter_besuch">
                        <br><br>
                        <label for="spam">Passwort- / Spamschutz: Bitte gib Internetsprachen ein</label><br>
                        <input type="password"
                               id="spam"
                               name="spamschutz"
                               required
                               autocomplete="off"><br><br>
                    </div>
                </fieldset>
                <fieldset>
                    <legend>Feedback</legend>
                    <br>
                    <div>
                        <label for="feedback">Was würden Sie verbessern</label>
                        <p>
                        <textarea id="feedback" name="feedback" rows="10" cols="40"></textarea>
                        </p>
                        <label for="commercial">Wie sind Sie auf diese Bewerbungsseite aufmerksam geworden</label>
                        <select name="commercial" id="commercial">
                            <optgroup label="Allgemein">
                                <option>Zufall</option>
                                <option>Freunde/Bekannte</option>
                                <option>Präsentation/Vortrag</option>
                                <option>Frühere Nutzung</option>
                            </optgroup>
                            <optgroup label="Internet-Angebote">
                                <option>Newsletter anderer Seiten</option>
                                <option>Suchmaschine</option>
                                <option>Linkliste</option>
                                <option>Forum/Gästebuch-Eintrag</option>
                                <option>Kostenlos/Gratis-Seite</option>
                            </optgroup>
                            <optgroup label="Medien">
                                <option>Zeitung</option>
                                <option>Zeitschrift</option>
                                <option>Radio</option>
                                <option>Fernsehen</option>
                            </optgroup>
                        </select>
                    </div>
                </fieldset>
                <fieldset>
                    <legend>Welche Note würdest Du allgemein dem Inhalt geben? (1 = sehr gut, 6 = ungenügend)</legend>
                    <input type="radio" id="note1" name="note" value="1" required>
                    <label for="note1">1 – sehr gut</label><br>
                    <input type="radio" id="note2" name="note" value="2">
                    <label for="note2">2 – gut</label><br>
                    <input type="radio" id="note3" name="note" value="3">
                    <label for="note3">3 – befriedigend</label><br>
                    <input type="radio" id="note4" name="note" value="4">
                    <label for="note4">4 – ausreichend</label><br>
                    <input type="radio" id="note5" name="note" value="5">
                    <label for="note5">5 – mangelhaft</label><br>
                    <input type="radio" id="note6" name="note" value="6">
                    <label for="note6">6 – ungenügend</label><br>
                </fieldset>
                <fieldset>
                    <legend>Welche Note würdest Du allgemein dem Aussehen geben? (1 = sehr gut, 6 = ungenügend)</legend>
                    <input type="radio" id="aussehen1" name="note_aussehen" value="1" required>
                    <label for="aussehen1">1 – sehr gut</label><br>
                    <input type="radio" id="aussehen2" name="note_aussehen" value="2">
                    <label for="aussehen2">2 – gut</label><br>
                    <input type="radio" id="aussehen3" name="note_aussehen" value="3">
                    <label for="aussehen3">3 – befriedigend</label><br>
                    <input type="radio" id="aussehen4" name="note_aussehen" value="4">
                    <label for="aussehen4">4 – ausreichend</label><br>
                    <input type="radio" id="aussehen5" name="note_aussehen" value="5">
                    <label for="aussehen5">5 – mangelhaft</label><br>
                    <input type="radio" id="aussehen6" name="note_aussehen" value="6">
                    <label for="aussehen6">6 – ungenügend</label><br>
                </fieldset>
                <fieldset>
                    <input type="submit" value="Absenden">
                    <input type="reset" value="Zurücksetzen">
                </fieldset>
            </form>
        </main>
        <?php include __DIR__ . "/Layout/Footer.php"; ?>
    </body>
</html>