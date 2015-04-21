package enemystates;

import model.entities.Enemy;

public class StateHandler {

	private EnemyState cur_state;
	private Enemy my_agent;

	public StateHandler(Enemy agent){
		my_agent = agent;
		cur_state = new AttackState();
	}
	
	//initialization
	public void setCurrentState(EnemyState new_state){
		cur_state = new_state;
	}
	
	public void update(int delta){
		if(cur_state != null){
			cur_state.execute(my_agent, delta);
		}
	}
	
	public void ChangeState(EnemyState state){
		if(state == null){
			return;
		}
		
		//exit the previous state
		cur_state.exit(my_agent);
		
		//change to the new state
		cur_state = state;
		
		//enter the new state
		cur_state.enter(my_agent);
	}
	
}
