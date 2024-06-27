OS := $(shell uname)
ARCH := $(shell uname -m)

NAME = Blocks
SRC = src
BIN = bin
LIB = lib
PACKAGE = com/pepebecker

run:
ifeq ($(OS), Darwin)
	@make run-macos
else
	@make run-linux
endif

run-macos: build
ifeq ($(ARCH), arm64)
	@java -cp $(BIN):$(LIB)/* -Djava.library.path=native/macosx/arm64 $(PACKAGE)/$(NAME)
else
	@java -cp $(BIN):$(LIB)/* -Djava.library.path=native/macosx/x86_64 $(PACKAGE)/$(NAME)
endif

run-linux: build
	@java -cp $(BIN):$(LIB)/* -Djava.library.path=native/linux $(PACKAGE)/$(NAME)

build: clean
	@mkdir -p $(BIN)/$(PACKAGE)
	@javac -d bin -sourcepath $(SRC) -cp .:lib/* $(SRC)/$(PACKAGE)/*.java
	@cp res/* $(BIN)/$(PACKAGE)/

clean:
	@rm -rf $(BIN)
