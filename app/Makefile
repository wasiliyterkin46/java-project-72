# Makefile
.DEFAULT_GOAL := build-run

run-dist: #  Запуск исполняемого файла
	make -C ./app run-dist

setup:
	make -C ./app setup

clean:
	make -C ./app clean

build:
	make -C ./app build

install:
	make -C ./app install

run:
	make -C ./app run

test:
	make -C ./app test

report:
	make -C ./app report

lint:
	make -C ./app lint

check-deps:
	make -C ./app check-deps


build-run: make -C ./app build run

.PHONY: build
