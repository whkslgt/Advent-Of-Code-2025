# Advent of Code 2025

My solutions for [Advent of Code 2025](https://adventofcode.com/2025) written in Kotlin.

## Project Structure

- `src/main/kotlin/gal/dario/advent2025/` - Solution implementations for each day
- `src/main/resources/` - Input data files

## Setup

This project uses Kotlin 2.2.20 with JVM toolchain 21.

### Prerequisites

- JDK 21 or later
- Gradle (wrapper included)

### Running Solutions

Compile and run a specific day's solution:

```bash
./gradlew compileKotlin && kotlin -cp build/classes/kotlin/main:build/resources/main gal.dario.advent2025.Day1Kt
```

Replace `Day1Kt` with the appropriate day (e.g., `Day2Kt`, `Day3Kt`, etc.)

### Running Benchmarks

This project includes JMH benchmarking support (Benchmarks are usually commented out).
To run benchmarks, please uncomment them and run:

```bash
./gradlew jmh
```

## Progress

- [x] Day 1
- [x] Day 2
- [x] Day 3
- [x] Day 4
- [x] Day 5
- [x] Day 6
- [x] Day 7
- [x] Day 8
- [x] Day 9
- [ ] Day 10
- [ ] Day 11
- [ ] Day 12

## License

See [LICENSE](LICENSE) file for details.
