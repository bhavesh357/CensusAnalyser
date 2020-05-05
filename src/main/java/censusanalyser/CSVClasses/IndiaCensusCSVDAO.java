package censusanalyser.CSVClasses;

public class IndiaCensusCSVDAO {
    public int area;
    public int density;
    public String state;
    public int population;

    public IndiaCensusCSVDAO(IndiaCensusCSV census) {
        this.state  = census.state;
        this.population = census.population;
        this.area = census.areaInSqKm;
        this.density = census.densityPerSqKm;
    }
}
