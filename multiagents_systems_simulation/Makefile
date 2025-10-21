# Compilation
SRC_FILES=$(wildcard src/main/*.java src/tests/*.java)
CLASSES=$(patsubst src/%.java,bin/%.class,$(SRC_FILES))

all: bin classes

# Crée le dossier bin si nécessaire
bin:
	mkdir -p bin

# Compile toutes les classes
classes: bin
	javac -d bin -cp ../lib/gui.jar $(SRC_FILES)

# Lancer l'application principale
run: all
	java -cp bin:../lib/gui.jar main.App

# Lancer les tests
test: all
	java -cp bin:../lib/gui.jar tests.TestBalls

# Nettoyer les fichiers compilés
clean:
	rm -rf bin
