# Multiagent Systems Simulation – POO 2A

Projet de simulation graphique de systèmes multi-agents en Java (ENSIMAG 2A – POO).

L’objectif est de simuler plusieurs modèles :
- mouvement de **balles** rebondissant sur les bords,
- **automates cellulaires** : Jeu de la Vie de Conway, Jeu de l’Immigration, modèle de **Schelling**,
- essaims de **boids** (agents mobiles) avec gestionnaire d’événements discrets.

L’application s’appuie sur :
- la librairie graphique fournie `lib/gui.jar` (classe `GUISimulator`, interface `Simulable`),
- un **Makefile** pour compiler / exécuter facilement,
- une architecture orientée objet (séparation modèle / vue / contrôleur, héritage, collections, etc.).
**Remarque ** : Afin de tester Invader, il faut revenir au repertoire de base 
---

## Arborescence du projet

```text
.
├── doc/                         # Documentation de gui.jar (html)
├── invader/                     # Exemple fourni (TestInvader)
├── lib/
│   └── gui.jar                  # Librairie graphique du simulateur
├── multiagents_systems_simulation/
│   ├── bin/                     # .class compilés
│   └── src/
│       ├── main/
│       │   ├── Automate/
│       │   │   ├── ConwayAndImmigration.java
│       │   │   ├── ConwaySimulator.java
│       │   │   ├── ImmigrationSimulator.java
│       │   │   ├── Schelling.java
│       │   │   └── SchellingSimulator.java
│       │   ├── Balls/
│       │   │   ├── Balls.java
│       │   │   └── BallsSimulator.java
│       │   ├── Boids/
│       │   │   ├── Behaviors/       # Règles : Cohésion, Alignement, Séparation, …
│       │   │   ├── Boids/           # Classes Boid, Bird, autres espèces
│       │   │   ├── BoidsSimulations/# Simulateurs de boids (Filles d'une abstract class Behavior)
│       │   │   └── Boidutils/       # Outils : Vector2D, Grid, FlowField, Path,Arrow,GridType,Line,Perlin,ImprovedNoise,PolygonGraphics,FlowFieldDraw
│       │   ├── EventManaging/
│       │   │   ├── Event.java
│       │   │   ├── EventBalls.java
│       │   │   ├── EventBoids.java
│       │   │   └── EventManager.java
│       │   └── App.java             # Point d’entrée possible pour lancer une démo globale
│       └── tests/
│           ├── TestBalls.java
│           ├── TestConway.java
│           ├── TestImmigration.java
│           ├── TestSchelling.java
│           ├── TestBoid.java
│           ├── TestBoids.java
│           ├── TestBoidsCohesion.java
│           └── TestBirdsCohesion.java
├── Makefile
└── README.md
```

## Architecture logique

### Modèle / Vue / Contrôleur

- Modèle :

`Balls`, `ConwayAndImmigration`, `Schelling`, `Boid`, `Bird`, etc.

→ contient l’état du système et les règles d’évolution.

- Vue :

GUISimulator (dans `gui.jar`) + formes (`Oval`, `Rectangle`, `Text`, `PolygonGraphics`, …).

→ se contente d’afficher ce que lui demandent les simulateurs.

- Contrôleurs (Simulators) :

`BallsSimulator`, `ConwaySimulator`, `ImmigrationSimulator`, `SchellingSimulator`, `BoidsSimulator`, …

→ implémentent `gui.Simulable` et redéfinissent :

    - next() : un pas de simulation (ou un appel au gestionnaire d’événements),

    - restart() : retour à l’état initial.

Chaque simulateur :

    - possède une référence sur le modèle,

    - possède une référence sur le GUISimulator,

    - traduit l’état du modèle en éléments graphiques.

## Modèles implémentés

### Simulations de balles (Balls)
Une première simulation dynamique permettant :
- de valider l’intégration avec `GUISimulator`,
- d’introduire la notion d’événements dans le temps,
- de séparer proprement modèle / contrôleur.

Le modèle **Balls.java** gère :
- la position des balles (objets `Point`),
- leur vitesse,
- les rebonds sur les bords,
- la réinitialisation.

Le contrôleur **BallsSimulator.java** :
- implémente `Simulable`,
- effectue l’appel à l’évolution (`update()`),
- redessine la scène à chaque pas.

Cette partie sert d’introduction aux simulations plus complexes.

---

### Automates cellulaires

Nous avons développé trois automates :
- **Jeu de la Vie de Conway**
- **Jeu de l’Immigration** (extensions multicolores)
- **Modèle de Ségrégation de Schelling**

#### Factorisation Conway / Immigration

Pour éviter la duplication de code, nous avons créé un modèle unique :

> `ConwayAndImmigration.java`

Il gère :

- la grille torique,
- le calcul du voisinage,
- la mise à jour simultanée via une grille temporaire,
- une règle paramétrable selon le nombre d’états **n** :
  - `n = 2` → Conway,
  - `n > 2` → Immigration.

Les simulateurs (`ConwaySimulator` et `ImmigrationSimulator`) ne font que :
- gérer l’affichage,
- appeler `updateGrid()`.

#### Modèle de Schelling

Le modèle `Schelling.java` utilise :
- une **ArrayList** pour stocker les maisons libres,
- un parcours en deux phases pour éviter les conflits :
  1. repérage des familles mécontentes,
  2. déplacement vers des logements vides mélangés (`Collections.shuffle()`).

Un indicateur de ségrégation (`segregationLevel()`) est affiché dans la console.

Le simulateur `SchellingSimulator.java` :
- redessine la grille avec `Oval`,
- permet de modifier le paramètre K visuellement.

---

### Modèle d’essaims (Boids)

Cette partie constitue le cœur avancé du projet.

#### Architecture Boids

Les boids sont divisés en sous-composants :

- **Boids/** : classes principales (`Boid`, `Bird`, autres espèces)
- **Behaviors/** : règles comportementales
  - Cohésion
  - Alignement
  - Séparation
  - Fuite / Poursuite
  - Wander, etc.
- **Boidutils/** : outils indispensables
  - `Vector2D`
  - `Grid` (accélération de la détection locale)
  - `FlowField` (déplacement guidé)
  - `PolygonGraphics` pour le rendu des agents
- **BoidsSimulations**

# Tests Boids

## Test d’un Boid

Ce test vérifie le fonctionnement du `BoidSimulator`, utilisant les
comportements *seek* et *wander*. Le simulateur reçoit un
`Vector2D target` fixe sur l’écran que le Boid suit.

**Comment exécuter :**

    make test-TestBoid

## Test de Boids

Ce test utilise le `BoidsSimulator` avec les comportements :

- Alignement,

- Séparation,

- Cohésion.

Les positions initiales sont générées aléatoirement.

**Comment exécuter :**

    make test-TestBoids

## Test Alignement / Séparation 

Chaque comportement est testé séparément.

**Comment exécuter :**

    make test-TestBoidsAlignement
    make test-TestBoidsCohesion

## Test de BoidWind

Ce test reprend le `BoidSimulator` mais ajoute un **vent** modélisé par
une grille de vecteurs. L’affichage peut être activé en passant `true`
lors du lancement.

Une classe `FlowField` a été créée pour cela. Le caractère dynamique est assuré par un modèle de Perlin et l'ajout d'un bruit.

**Comment exécuter :**

    make test-TestBoidWithWind

## TestEagleChase

Deux classes filles de `Boid` ont été ajoutées : `Eagle` et `Bird`.

- `Bird` fuit lorsqu’il détecte un `Eagle` à une distance
  `detectionRadius`.

- L’Eagle détecte et poursuit les Birds dans ce rayon.

- Si la distance devient inférieure à `killRadius`, l’oiseau est
  considéré comme mangé et retiré de la simulation.

La classe `Eagle` implémente `hunt`, enregistre et supprime les Birds
capturés. Le test utilise un `MultipleBoidsSimulator` gérant plusieurs
groupes simultanément.

**Comment exécuter :**

    make test-TestEagleChase

## TestBirdCohesion

Teste la **Cohesion** des Birds

**Comment exécuter :**

    make test-TestBirdCohesion

## TestBirdCohesionWithWind

Teste le comportement des **Birds** mais avec cette fois du vent comme dans **TestBoidWithWind**

**Comment exécuter :**

    make test-TestBirdCohesionWithWind

## TestWolfChase

Deux autres classes filles de `Boid` ont été créées : `Wolf` et `Deer`.

### Comportement des Wolves

- Ils suivent un leader, choisi comme celui le plus proche d’une proie
  (`Deer`).

- Si aucun Wolf ne détecte de proie, un leader est choisi aléatoirement.

- Si la distance devient inférieure à `slowRadius`, ils encerclent.

- Sous `killRadius`, la proie est considérée morte, on la supprime de l'interface graphique et un nouveau leader
  est choisi.

- Le leader est mis à jour à chaque `next`.

### Comportement des Deer

- Le Deer est solitaire et migrateur.

- Retrait de Cohesion et Alignement.

- Utilisation de `FleeFromPredator` et `FollowPath`.

- Le chemin est régénéré si la distance au dernier point devient
  inférieure à `distancetoArrival`.

**Comment exécuter :**

    make test-TestWolfChase (Il est préférable d'augmenter la vitesse d'exécution du simulateur)
#### Gestion du temps : EventManager

Les boids sont mis à jour par un gestionnaire d’événements discrets, imposé par le sujet.

Le gestionnaire :
- conserve les événements dans un `PriorityQueue`,
- garantit l'exécution **par ordre chronologique**,
- planifie automatiquement l’événement de mise à jour des boids (`EventBoids`).

Ainsi, la simulation ne dépend plus des clics sur `next()` mais avance à son propre rythme.

---

- Équipe :

Projet réalisé par :

    - IKNE Mouad

    - EL GOUIJ Faical

    - MOUNTASSIR Hamza

    - TOUATI Ayoub

    - J’MEILI Makhtour

