.PHONY: run ps up down

run:
	./mvnw spring-boot:run

ps:
	docker compose ps

up:
	docker compose up -d

down:
	docker compose down
