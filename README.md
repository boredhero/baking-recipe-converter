# baking-recipe-converter

### About

I doubt this will be of any use to anyone. My father was a chef/baker, and I have a collection of recipes from him. Most of these call for extremely large amounts of ingredients. I.E., 50-75lbs flour. I use this to resize recipes while maintaining ingredient ratios. It's a simple commandline only program that imports a specifically formatted CSV and exports a results CSV.

### Syntax
* `java -jar baking-recipe-converter.jar /path/to/file.csv`
  * `java -jar baking-recipe-converter.jar /path/to/file/.csv diag` to print a ton of diagnostic output from the program.
* `java -jar baking-recipe-converter.jar formatting`
  * Gives a manpage on how to format your `input.csv` files. Also see the included example in this repo.
* `java -jar baking-recipe-converter.jar about` OR `java -jar baking-recipe-converter.jar version`
  * Program name and version of your file, credits, license.

### Building
* `chmod +x gradlew` & `./gradlew build`
  * Artifacts will appear in `/build/libs`
