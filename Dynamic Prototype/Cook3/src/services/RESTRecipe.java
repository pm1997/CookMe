package services;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import services.Recipe;
import static services.Constant.GET_NAV_TITLES;
import static services.Constant.GET_CATEGORIES;

import static services.Constant.TYPE_CATEGORY;
import static services.Constant.TYPE_RECIPE;

@Path("/recipe")
@Produces(MediaType.APPLICATION_JSON)
public class RESTRecipe extends DatabaseAdapter {

	@GET
	@Path("/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String getRecipe(@PathParam("id") int id) {
		String where = "id = " + id;
		String select = "id,title,ingredients,rauthor,description,category,nutritionfacts";
		String database = "cookme.recipe";
		int type = TYPE_RECIPE;
		if (id == GET_NAV_TITLES) {
			where = "id = id";
			select = "id,title,category";
		}
		if (id == GET_CATEGORIES) {
			where = "id = id";
			select = "id,categoryname";
			database = "cookme.categories";
			type = TYPE_CATEGORY;
		}
		DatabaseResponse responce = select(type, database, select, where);
		if (responce == null) {
			return "empty";
		}
		// for (String title : responce.getTitle()) {
		// System.out.println("----------"+ title);
		// }
		JsonArray recipesJson = new JsonArray();
		if (id == GET_CATEGORIES) {
			List<RecipeCategory> list = responce.toRecipeCategoryList();
			if (list.isEmpty()) {
				System.out.println("Kategorie nicht vorhanden:");
				return "empty";
			}

			for (RecipeCategory recipeCategory : list) {
				JsonObject rJson = new JsonObject();
				rJson.addProperty("name", recipeCategory.getCategoryName());
				rJson.addProperty("id", recipeCategory.getId());
				recipesJson.add(rJson);
			}
		} else {

			List<Recipe> list = responce.toRecipeList();
			if (list.isEmpty()) {
				System.out.println("Rezept nicht vorhanden:");
				return "empty";
			}

			for (Recipe recipe : list) {
				JsonObject rJson = new JsonObject();
				rJson.addProperty("title", recipe.getTitle());
				rJson.addProperty("id", recipe.getId());
				rJson.addProperty("category", recipe.getCategoryId());
				if (id != GET_NAV_TITLES) {
					rJson.addProperty("description", recipe.getDescription());
					rJson.addProperty("ingredients", recipe.getIngredients());
					rJson.addProperty("nutritionfacts", recipe.getNutritionFacts());
				}
				recipesJson.add(rJson);
			}
		}
		return recipesJson.toString();
	}

	@PUT
	// @Path("/{customerMail}/{customerPassword}")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String updatePerson(@FormParam("id") int id, @FormParam("username") String userName) {
		if (!update(1, "cookme.person", "username", "username = 'patrick'")) {
			return "Das Objekt ist nicht Vorhanden in der DB.";
		} else
			return "Success";
	}

	@POST
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String insertRecipe(@FormParam("title") String title, @FormParam("ingredients") String ingredients,
			@FormParam("description") String description, @FormParam("category") String category) {
		String insert = " `recipe` ( `title`,`description`,`ingredients`,`category`) ";
		String values = "'" + title + "', '" + description + "','" + ingredients + "'," + category + "";
		if (!insert(TYPE_RECIPE, insert, values)) {
			// dbs.insert(t);
			return "Error wrong username or password";
		} else
			return "Login sucessfull";
	}

	@DELETE
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.TEXT_PLAIN)
	public String deleteTPerson(@FormParam("password") String password, @FormParam("username") String userName) {
		System.out.println("DELETE----------");
		if (delete(1, userName, password)) {
			return "Success";
		} else
			return "Das Objekt ist nicht Vorhanden in der DB.";
	}

}
