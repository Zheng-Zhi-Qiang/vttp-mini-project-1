package visa.vttpminiproject1.models;

import java.util.List;

public class Portfolio {
    private List<Position> positions;

    public List<Position> getPositions() {
        return positions;
    }

    public void setPositions(List<Position> positions) {
        this.positions = positions;
    }

    public Double getNAV() {
        Double nav = positions.stream()
                .map(position -> position.getMarketValue())
                .reduce(0.0, (a, b) -> a + b);
        return nav;
    }
}
