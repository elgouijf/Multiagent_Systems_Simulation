SRC_FILES := $(shell find src -name "*.java")
CLASSES=$(patsubst src/%.java,bin/%.class,$(SRC_FILES))

all: bin classes

bin:
	@mkdir -p bin

classes: bin
	@echo ">>> Compilation en cours..."
	@javac -d bin -cp ../lib/gui.jar $(SRC_FILES)
	@echo ">>> Compilation terminée."

run: all
	@java -cp bin:../lib/gui.jar main.App


test-%: all
	@echo ">>> Recherche du test $* ..."
	@CLASS=$$(find src/tests -name "$*.java" | sed 's/src\///; s/\.java//' | tr '/' '.'); \
	 if [ -z "$$CLASS" ]; then \
	    echo ">>> ERREUR : Aucun test trouvé pour $*"; \
	 else \
	    echo ">>> Test détecté : $$CLASS"; \
	    java -cp bin:../lib/gui.jar $$CLASS; \
	 fi

clean:
	@rm -rf bin
	@echo ">>> Dossier bin supprimé."
