package Board;

public class Position implements Comparable<Position> {
    public char x;
    public int y;

    public Position(char x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position(String position) {
        this.x = position.charAt(0);
        this.y = Integer.parseInt(position.substring(1));
    }

    public boolean equals(Object o) {
        if (!(o instanceof Position comparedPosition))
            return false;
        return x == comparedPosition.x && y == comparedPosition.y;
    }

    public boolean isValid() {
        return x <= 'H' && x >= 'A' && y <= 8 && y > 0;
    }

    @Override
    public String toString() {
        return "" + x + y;
    }

    @Override
    public int compareTo(Position position) {
        if (position == null) {
            throw new NullPointerException();
        }
        if (y == position.y) {
            return x - position.x;
        }
        return y - position.y;
    }
}
