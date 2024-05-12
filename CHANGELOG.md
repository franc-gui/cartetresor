# Changelog

All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Added

- Empêcher un aventurier de commencer sur une montagne
- Empêcher un aventurier de commencer en dehors de la carte
- Empêcher un trésor de se retrouver sur une montagne
- Empêcher un trésor de se retrouver en dehors de la carte
- Empêcher une montagne de se retrouver en dehors de la carte
- Empêcher de mettre plusieurs cartes en entrée

### Changed

- Améliorer la gestion des erreurs (faire plus de FunctionalErrorException), certains fichiers avec des inputs incorrects peuvent créer des erreurs non-compréhensibles
- Remplacer tous les caractères par des Enum (à l'instar d'Orientation)

## [0.0.1] - 2024-05-11

### Added

- Traitement carte au trésor

[0.0.1]: https://github.com/franc-gui/cartetresor/releases/tag/v0.0.1
