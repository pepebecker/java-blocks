OS := $(shell uname)

NAME = Blocks
SRC = src
BIN = bin
LIB = lib
PACKAGE = com/pepebecker

build:
	@mkdir -p $(BIN)/$(PACKAGE)
	@javac -d bin -sourcepath $(SRC) -cp .:lib/* $(SRC)/$(PACKAGE)/*.java
	@cp res/* $(BIN)/$(PACKAGE)/

run:
ifeq ($(OS), Darwin)
	@java -cp $(BIN):$(LIB)/* -Djava.library.path=native/macosx $(PACKAGE)/$(NAME)
else ifeq ($(OS), Linux)
	@java -cp $(BIN):$(LIB)/* -Djava.library.path=native/linux $(PACKAGE)/$(NAME)
endif

clean:
	@rm -rf $(BIN)