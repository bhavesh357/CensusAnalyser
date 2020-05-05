package censusanalyser;

import censusanalyser.CSVClasses.CSVStates;

public class CSVStatesDAO {
    public String stateCode;
    public String state;

    public CSVStatesDAO(CSVStates census) {
        state=census.state;
        stateCode=census.stateCode;
    }
}
