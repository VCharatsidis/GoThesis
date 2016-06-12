package minimax;


import lombok.AllArgsConstructor;
import lombok.Getter;
import ai.game_abstractions.GameMove;

@Getter
@AllArgsConstructor
public class GoMove extends GameMove<GoState> {
	private int row;
	private int col;

	public boolean isPass() {
		return (row == -1 && col == -1);
	}
}
