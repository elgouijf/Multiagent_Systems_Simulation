

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
	@echo ">>> Exécution du test $* ..."
	@java -cp bin:../lib/gui.jar tests.$*
	@echo ">>> Test terminé."


clean:
	@rm -rf bin
	@echo ">>> Dossier bin supprimé."
