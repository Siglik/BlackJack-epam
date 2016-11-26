/**
 * 
 */
package org.qqq175.blackjack.service.action.implemented;

import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.add.EditCrewAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.add.NewCityAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.add.NewEmployeeAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.add.NewFlightAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.add.NewHubAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.add.NewRouteAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.add.SaveCrewAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.basic.EmptyAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.basic.LoginAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.basic.LogoutAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.delete.DeleteCityAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.delete.DeleteEmployeeAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.delete.DeleteFlightAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.delete.DeleteHubAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.delete.DeleteRouteAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.show.ShowAllCitiesAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.show.ShowAllFlightsAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.show.ShowAllHubsAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.show.ShowAllRoutesAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.implemented.show.ShowEmployeesAction;
import org.qqq175.it_academy.jd1.airline_web.service.actions.interfaces.Action;

/**
 * @author qqq175
 *
 */
public enum ActionEnum {
	NONE {
		{
			this.action = new EmptyAction();
		}
	},
	UNKNOWN {
		{
			this.action = new EmptyAction();
		}
	},
	LOGIN {
		{
			this.action = new LoginAction();
		}
	},
	LOG_OUT {
		{
			this.action = new LogoutAction();
		}
	},
	SHOW_FLIGHTS {
		{
			this.action = new ShowAllFlightsAction();
		}
	},
	SHOW_ROUTES {
		{
			this.action = new ShowAllRoutesAction();
		}
	},
	SHOW_CITIES {
		{
			this.action = new ShowAllCitiesAction();
		}
	},
	SHOW_HUBS {
		{
			this.action = new ShowAllHubsAction();
		}
	},
	SHOW_EMPLOYEES {
		{
			this.action = new ShowEmployeesAction();
		}
	},
	NEW_CITY {
		{
			this.action = new NewCityAction();
		}
	},
	DELETE_CITY {
		{
			this.action = new DeleteCityAction();
		}
	},
	NEW_HUB {
		{
			this.action = new NewHubAction();
		}
	},
	DELETE_HUB {
		{
			this.action = new DeleteHubAction();

		}
	},
	NEW_ROUTE {
		{
			this.action = new NewRouteAction();
		}
	},
	DELETE_ROUTE {
		{
			this.action = new DeleteRouteAction();
		}
	},
	SHOW_ROUTE {
		{
		}
	},
	NEW_FLIGHT {
		{
			this.action = new NewFlightAction();
		}
	},
	DELETE_FLIGHT {
		{
			this.action = new DeleteFlightAction();
		}
	},
	EDIT_FLIGHT_CREW {
		{
			this.action = new EditCrewAction();
		}
	},
	SAVE_CREW {
		{
			this.action = new SaveCrewAction();
		}
	},
	NEW_EMPLOYEE {
		{
			this.action = new NewEmployeeAction();
		}
	},
	DELETE_EMPLOYEE {
		{
			this.action = new DeleteEmployeeAction();
		}
	};
	Action action;

	public Action getAction() {
		return action;
	}
}
