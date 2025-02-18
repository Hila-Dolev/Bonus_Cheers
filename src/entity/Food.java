package entity;


import java.util.Objects;

public class Food {
	private int foodID; //PK
	private String nameOfDish;
	private String RecipeLink1;
	private String RecipeLink2;
	private String RecipeLink3;
	private String RecipeLink4;
	private String RecipeLink5;
	
	//Constructor

	public Food(int foodID, String nameOfDish, String recipeLink1, String recipeLink2, String recipeLink3, String recipeLink4,
			String recipeLink5) {
		super();
		this.foodID = foodID;
		this.nameOfDish = nameOfDish;
		RecipeLink1 = recipeLink1;
		RecipeLink2 = recipeLink2;
		RecipeLink3 = recipeLink3;
		RecipeLink4 = recipeLink4;
		RecipeLink5 = recipeLink5;
	}

	
	public int getFoodID() {
		return foodID;
	}


	public void setFoodID(int foodID) {
		this.foodID = foodID;
	}


	public String getNameOfDish() {
		return nameOfDish;
	}

	public void setNameOfDish(String nameOfDish) {
		this.nameOfDish = nameOfDish;
	}

	public String getRecipeLink1() {
		return RecipeLink1;
	}

	public void setRecipeLink1(String recipeLink1) {
		RecipeLink1 = recipeLink1;
	}

	public String getRecipeLink2() {
		return RecipeLink2;
	}

	public void setRecipeLink2(String recipeLink2) {
		RecipeLink2 = recipeLink2;
	}

	public String getRecipeLink3() {
		return RecipeLink3;
	}

	public void setRecipeLink3(String recipeLink3) {
		RecipeLink3 = recipeLink3;
	}

	public String getRecipeLink4() {
		return RecipeLink4;
	}

	public void setRecipeLink4(String recipeLink4) {
		RecipeLink4 = recipeLink4;
	}

	public String getRecipeLink5() {
		return RecipeLink5;
	}

	public void setRecipeLink5(String recipeLink5) {
		RecipeLink5 = recipeLink5;
	}

	@Override
	public int hashCode() {
		return Objects.hash(RecipeLink1, RecipeLink2, RecipeLink3, RecipeLink4, RecipeLink5, nameOfDish);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Food other = (Food) obj;
		return Objects.equals(RecipeLink1, other.RecipeLink1) && Objects.equals(RecipeLink2, other.RecipeLink2)
				&& Objects.equals(RecipeLink3, other.RecipeLink3) && Objects.equals(RecipeLink4, other.RecipeLink4)
				&& Objects.equals(RecipeLink5, other.RecipeLink5) && Objects.equals(nameOfDish, other.nameOfDish);
	}
	
	
	
	
	
}
