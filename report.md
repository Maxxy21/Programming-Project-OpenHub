PP201920_Project_D2 Project Report
=====

This report describes in details and the architecture of the application by focusing on:

* The architecture of the application and how different programming techniques were applied,
* Examples of files produced by the application and
* Difficulties faced by each group member.

## Architecture PP201920_Project_A6

This java application fetches activity data from an open api (Open Data Hub South Tyrol). 
The amount of activities fetched is determined by the input.txt file. 
The fetched json data gets mapped to activities and saved in the result's folder. 
The application also validates the json schemas and creates an analysis.json file.
After the execution we have a list of activity files in the result's folder. 


## How different programming techniques were applied

We applied following techniques:

- **Multithreading** Parallel fetching and validating of data
- **Collections** We used Maps and Lists to handle the json data
- **Classes** to represent the data from the api and to structure our Application
- **Streams** to read the Http Responses
- **Exceptions**
- **Logging** we log errors and info for the user
- **Http Requests**
- **Testing**

We used the following dependencies:
- **log4j**
- **commons-lang**
- **maven-compiler-plugin**
- **json-schema-validator**
- **junit-jupiter-engine**
- **junit**
- **slf4j**
- **jackson-databind**
- **gson**


## Example Activity.json
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

## Example Analysis.json
```json
{
  "activitiesTypes": {
    "loipen": 223,
    "klassisch und skating": 195,
    "radfahren": 391,
    "mountainbikes": 238,
    "laufen und fitness": 197,
    "laufstrecken": 111,
    "bergläufe": 34
  },
  "trackedActivityIds": [
    "01CFEFF8DA586E548327E539276C42F3",
    "B66FA66DA650717E0964A4E30A716DAE",
    "A390A8B7599E4E2C43D053CC1753ABE2",
    "09B0D0DEC4E9955982EC480ADDEF4A00",
    "FDCFC5CA5F682BC1B48B8C01A0EFE2ED",
    "F533E41D900D47C9E8F1636EC8335DAB",
    "1876385E20241446C38D20892F21E20F",
    "75EEA45D4DA78F51435C2744F1DBDD7B",
    "09DF49C17913B103C200557F286112BB",
    "CEF07D5EAB5EA68D0A60B50B30E23027",
    "240837BAEB06A115DF41D1E09346955A",
    "8A707773182DFA56FF5C5FB0688510D1",
    "95BE62AA53DDBB9648FF81BE5B4140FE",
    "B647B33CC05D573F18B77BAD3B011A15",
    "B7FCAED8B20BCC7258984FF5119F4783",
    "5DB9D62E7202B2CC735BA40C7F1A1945",
    "027BDAB195CE66B25C0AAB3D8EF776CB",
    "2F5B909071C474C5C82F31B46A67E359",
    "93DF039F28C6C8BA5F3D628B2736DB78"
  ],
  "regionsWithMostActivities": {
    "numberOfActivities": 15,
    "regionIds": [
      "D2633A20C24E11D18F1B006097B8970B"
    ]
  },
  "regionsWithLeastActivities": {
    "numberOfActivities": 3,
    "regionIds": [
      "D2633AAAA345F18F1A006096B4770A"
    ]
  }
}
```
## Challenges
* **Lukas Berger**: My biggest challenge was the creation of an Application as a Team. You have to change your way of 
thinking while writing code and try to make your code work with the rest of the code. When writing Code alone you need 
to come up with a solution for every problem and all parts come together exactly as you want them. In a team the other 
members create their part, and you have to implement your code to work with their parts. It was an important experience 
to learn how other developers solve problems.
* **Maxwell Aboagye**: My biggest challenge was the error (Java Heap Space) I was getting when executing the program 
with the maximum number of activity locations. I found out that the problem lied in the appending of objects, mostly 
with the Java native HTTP library, so I tested with other libraries and found that java.net was fast and run without 
problems. I was not getting the error anymore, but it required Java 11 to run, which would be incompatible with the 
specified Maven Java version of the project. I then consulted my professor and online forums and found out that I could
specify memory to be to allocated to JVM for the execution. Before that I asked a group member to run the program on his 
machine, and it worked perfectly without errors. He told me he had done nothing particular for JVM on his machine. So I 
had a thought that maybe it was the Java version on my machine. I went to check, and it was a 32-bit version, so I 
downloaded the 64-bit version, and it works okay now on my machine.
