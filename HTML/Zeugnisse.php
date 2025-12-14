<!DOCTYPE html>
<html lang="de">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <link rel="stylesheet" href="../CSS/Print.css" media="print">
        <link rel="stylesheet" type="text/css" href="../CSS/Desktop.css">
        <link rel="stylesheet" type="text/css" href="../CSS/Mobile.css">
        <link rel="stylesheet" type="text/css" href="../CSS/Tablet.css">
        <title>Zeugnisse - Amer Abo Alshalat</title>
    </head>
    <body>
        <?php include __DIR__ . "/Layout/Navigation.php"; ?>
        <main>
            <h1>Zeugnisse</h1>
            <form action="mailto:mr.ameraboalshalat@gmail.com?subject=Zeugnissbestellung" method="post" enctype="text/plain">
                <fieldset>
                    <legend>Persönliche Daten</legend>
                    <p>
                        <label for="name">Name:</label>
                        <input id="name" name="name" type="text" required>
                    </p>
                    <p>
                        <label for="adresse">Adresse:</label>
                        <input id="adresse" name="adresse" type="text" size="40" required>
                    </p>
                </fieldset>
                <fieldset>
                    <legend>Gewünschte Unterlagen</legend>
                    <p>
                        <input id="notenspiegel" name="unterlagen" type="checkbox" value="notenspiegel">
                        <label for="notenspiegel">Notenspiegel</label>
                    </p>
                    <p>
                        <input id="arbeitzeugniss" name="unterlagen" type="checkbox" value="arbeitzeugniss">
                        <label for="arbeitzeugniss">Arbeitgeber Zeugnisse</label>
                    </p>
                    <p>
                        <input id="betAusbildung" name="unterlagen" type="checkbox" value="praktikum">
                        <label for="betAusbildung">Betriebliche Ausbildung zeugnisse</label>
                    </p>
                    <p>
                        <input id="IHKAusbildung" name="unterlagen" type="checkbox" value="IHKAusbildung">
                        <label for="IHKAusbildung">IHK Ausbildung zeugnisse</label>
                    </p>
                    <p>
                        <input id="syrUni" name="unterlagen" type="checkbox" value="syrUni">
                        <label for="syrUni">Syrische virtuelle Universität zeugnisse</label>
                    </p>
                    <p>
                        <input id="syrTechDip" name="unterlagen" type="checkbox" value="syrTechDip">
                        <label for="syrTechDip">Technisches Diplom in Computer Engineering zeugnisse</label>
                    </p>
                </fieldset>
                <fieldset>
                    <legend>Versand</legend>
                    <p>
                        <label for="anzahl">Anzahl Kopien:</label>
                        <input id="anzahl" name="anzahl" type="number" min="1" max="10" value="1">
                    </p>
                    <p>
                        <label for="versand">Versandart:</label>
                        <select id="versand" name="versand">
                            <option>Standardbrief</option>
                            <option>Einschreiben</option>
                        </select>
                    </p>
                </fieldset>
                <p>
                    <label for="bemerkungen">Bemerkungen:</label><br>
                    <textarea id="bemerkungen" name="bemerkungen" rows="4" cols="40"></textarea>
                </p>
                <p>
                    <button type="submit">Bestellung absenden</button>
                    <button type="reset">Zurücksetzen</button>
                </p>
            </form>
        </main>
        <?php include __DIR__ . "/Layout/Footer.php"; ?>
    </body>
</html>