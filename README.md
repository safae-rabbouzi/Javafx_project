# Rapport du projet d'application de gestion des inscriptions des étudiants utilisant JavaFX
## Introduction

Ce  projet est une application de gestion des inscriptions des étudiants, réalisé en utilisant JavaFX comme framework pour l'interface utilisateur. L'application permet d'effectuer les opérations CRUD (Create, Read, Update, Delete) sur les données des étudiants, notamment leur nom, prénom, adresse e-mail, CNE (Code National de l'Étudiant), âge, filière et année d'études.

## Project
Créer par : <br/>
      <a href="https://www.linkedin.com/in/zineb-essoussi-5301581b6/">**Zineb Essoussi**</a> <br/>
     <a href="https://www.linkedin.com/in/safae-rabbouzi-b80621209/"> **Safae Rabbouzi**</a>
      
https://github.com/DesignToWebsite/Javafx_project/assets/74991230/78f89ed6-3374-47c4-929d-a970afac5779


## Description du projet

Le projet consiste en une application de bureau basée sur JavaFX, qui fournit une interface utilisateur conviviale pour interagir avec la base de données des étudiants. Voici une description des différentes parties de l'application :
1. Classe principale (Main) :
      La classe Main étend la classe Application de JavaFX et représente le point d'entrée de l'application.
      Elle initialise la fenêtre principale de l'application, configure les colonnes de la table des étudiants, crée les boutons et définit les événements associés.

2. Interface utilisateur :
    La fenêtre principale de l'application affiche une table contenant les informations des étudiants et des boutons pour effectuer les opérations CRUD.
    Les informations des étudiants sont organisées en colonnes dans la table, telles que l'identifiant, le nom, le prénom, l'adresse e-mail, le CNE, l'âge, la filière et l'année d'études.
    Les boutons "Add Student", "Update Student", "Delete Student", "View Student Profile" et "Print Summary Sheet" permettent d'effectuer différentes actions sur les étudiants.

3. Gestion des événements :
    Les boutons de l'interface utilisateur sont associés à des événements spécifiques, qui déclenchent des actions correspondantes.
    L'ajout d'un étudiant ou la mise à jour de ses informations ouvre une fenêtre de dialogue pour saisir les données nécessaires.
    La suppression d'un étudiant affiche une confirmation avant de supprimer définitivement l'enregistrement de la base de données.

4. Accès à la base de données :
    La classe DatabaseConnection fournit une connexion à la base de données SQLite contenant les informations des étudiants.
    La méthode **loadStudents()** récupère les étudiants depuis la base de données et les ajoute à la liste observable utilisée par la table.
    La méthode **openAddStudentLayout()** permet d'ajouter un nouvel étudiant en insérant ses informations dans la base de données.
    La méthode **openUpdateStudentLayout()** ouvre une fenêtre de dialogue pour mettre à jour les informations d'un étudiant sélectionné.
    La méthode **openDeleteStudentLayout()** affiche une confirmation avant de supprimer un étudiant sélectionné de la base de données.
    la méthode **openViewStudentLayout()** affiche le profil de l'étudiant sélectionné qui contient ses informations.
    
5. Impression d'un résumé des étudiants :
    Le bouton **"Print Summary Sheet"** déclenche l'événement **printSummarySheet()**, qui génère un fichier PDF contenant un résumé des informations des étudiants.
    La bibliothèque iText est utilisée pour créer le fichier PDF à partir des données des étudiants.

## Conclusion
Le projet d'application de gestion des inscriptions des étudiants réalisé en utilisant JavaFX offre une solution pratique pour gérer les informations des étudiants, notamment leur ajout, leur mise à jour, leur suppression et offre des fonctionnalités supplémentaires telles que l'affichage du profil et l'impression de la fiche d'étudiant.
