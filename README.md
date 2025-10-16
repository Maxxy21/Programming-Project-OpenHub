# OpenHub Activity Data Analyzer

A Java application that fetches, processes, and analyzes activity data from the [Open Data Hub South Tyrol](https://opendatahub.com/) API. The application retrieves tourism activity information, performs statistical analysis, and validates all data against JSON schemas.

## Features

- **API Data Fetching**: Retrieves activity data from Open Data Hub South Tyrol API
- **Multithreaded Processing**: Parallel execution for efficient data fetching and validation
- **Multilingual Support**: Handles activity data in English, Italian, and German
- **Data Analysis**: Generates comprehensive statistics including:
  - Activity type distribution
  - GPS-tracked activities identification
  - Regional activity analysis (most/least active regions)
- **JSON Schema Validation**: Ensures data integrity with automatic validation
- **Structured Output**: Generates individual activity files and aggregated analysis reports

## Prerequisites

- Java 8 or higher
- Maven 3.x

## Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/Programming-Project-OpenHub.git
cd Programming-Project-OpenHub
```

2. Install dependencies:
```bash
mvn clean install
```

## Usage

### Configuration

Set the number of activities to fetch by editing `src/main/resources/input.txt`:
```
10
```

### Running the Application

**Using Maven:**
```bash
mvn exec:exec
```

**Using the JAR file:**
```bash
mvn package
java -jar target/PP201920_Project_A6-1.0-SNAPSHOT-jar-with-dependencies.jar
```

### Output

The application generates files in the `results/` directory:

- `Activity_{id}.json` - Individual activity files containing:
  - Activity ID, name, and description
  - Activity types and categories
  - GPS tracking availability
  - Regional information

- `analysis.json` - Aggregated analysis containing:
  - Activity type distribution
  - List of GPS-tracked activities
  - Regions with most/least activities

## Example Output

**Activity File:**
```json
{
  "id": "00AF3709F5AD04FC4A07CDD829C612D6",
  "name": "Rudi Rentier Weg",
  "types": [
    "piste",
    "weitere pisten",
    "ski alpin"
  ],
  "hasGpsTrack": true,
  "region": "Dolomites Region Three Peaks"
}
```

**Analysis File:**
```json
{
  "activitiesTypes": {
    "loipen": 223,
    "klassisch und skating": 195,
    "radfahren": 391
  },
  "trackedActivityIds": [
    "01CFEFF8DA586E548327E539276C42F3",
    "B66FA66DA650717E0964A4E30A716DAE"
  ],
  "regionsWithMostActivities": {
    "numberOfActivities": 15,
    "regionIds": [
      "D2633A20C24E11D18F1B006097B8970B"
    ]
  }
}
```

## Development

### Running Tests

```bash
mvn test
```

### Running a Single Test

```bash
mvn test -Dtest=ClassName#methodName
```

### Building

```bash
mvn package
```

### Generating Javadoc

```bash
mvn javadoc:javadoc
```

### Cleaning Build Artifacts

```bash
mvn clean
```

## Architecture

### Project Structure

```
src/
├── main/
│   ├── java/project/
│   │   ├── mappers/          # Data models
│   │   │   ├── apiClasses/   # API response POJOs
│   │   │   ├── Activity.java
│   │   │   └── Analysis.java
│   │   └── runners/          # Main application logic
│   │       ├── Runner.java           # Entry point
│   │       ├── ApiDataFetcher.java   # HTTP client
│   │       ├── ActivitiesAnalysis.java
│   │       └── Validator.java
│   └── resources/
│       ├── input.txt             # Configuration
│       ├── Activities.schema.json
│       └── Analysis.schema.json
└── test/
    ├── java/                 # Test files
    └── resources/            # Test fixtures
```

### Execution Flow

1. **Data Fetching Thread**:
   - Reads page size from `input.txt`
   - Fetches JSON data from Open Data Hub API
   - Maps API response to Activity objects
   - Generates analysis statistics
   - Writes JSON files to `results/` directory

2. **Validation Thread**:
   - Waits for data fetching to complete
   - Validates all generated JSON files against schemas
   - Logs validation results

### Key Technologies

- **Gson 2.11.0** - JSON serialization/deserialization
- **Jackson 2.18.2** - JSON schema validation
- **Log4j 2.24.3** - Logging framework
- **JUnit 5.11.4** - Testing framework
- **json-schema-validator 1.5.3** - JSON Schema validation

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## Authors

- **Maxwell Aboagye** - Core development
- **Lukas Berger** - Core development

## License

This project was developed as part of the PP201920 Programming Project course.

## Acknowledgments

- Data provided by [Open Data Hub South Tyrol](https://opendatahub.com/)
- Built for the Programming Project (PP201920) course
