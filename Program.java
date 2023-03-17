import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Stream;

interface ItemPrinter
{
	void print(String text);
}

interface Printer
{
	ItemPrinter item();
}

interface MenuItem
{
	boolean isName(String name);
	void print(Printer printer);
}

class FoodItem implements MenuItem
{
	private String name;
	private float weight;
	private int calories;
	private int price;

	public FoodItem(String name, float weight, int calories, int price)
	{
		this.name = name;
		this.weight = weight;
		this.calories = calories;
		this.price = price;
	}

	@Override
	public boolean isName(String name)
	{
		return name.equals(this.name);
	}

	@Override
	public void print(Printer printer)
	{
		ItemPrinter item = printer.item();
		item.print("Name : " + name);
		item.print("Weight : " + weight);
		item.print("Calories : " + calories);
		item.print("Price : " + price);
	}
}

class DrinkItem implements MenuItem
{
	private String name;
	private float volume;
	private int calories;
	private int price;

	public DrinkItem(String name, float volume, int calories, int price)
	{
		this.name = name;
		this.volume = volume;
		this.calories = calories;
		this.price = price;
	}

	@Override
	public boolean isName(String name)
	{
		return name.equals(this.name);
	}

	@Override
	public void print(Printer printer)
	{
		ItemPrinter item = printer.item();
		item.print("Name : " + name);
		item.print("Volume : " + volume);
		item.print("Calories : " + calories);
		item.print("Price : " + price);
	}
}

class MusicItem implements MenuItem
{
	public MusicItem(String name, String author, float length)
	{
		this.name = name;
		this.author = author;
		this.length = length;
	}

	private String name;
	private String author;
	private float length;

	@Override
	public boolean isName(String name)
	{
		return name.equals(this.name);
	}

	@Override
	public void print(Printer printer)
	{
		ItemPrinter item = printer.item();
		item.print("Name : " + name);
		item.print("Author : " + author);
		item.print("Length : " + length + " min");
	}
}

class SpecialMenu implements MenuItem
{
	private String name;
	private MenuItem[] items;

	public SpecialMenu(String name, MenuItem[] items)
	{
		this.name = name;
		this.items = items;
	}
	@Override
	public boolean isName(String name)
	{
		return name.equals(this.name);
	}
	@Override
	public void print(Printer printer)
	{
		for(MenuItem item : items) item.print(printer);
	}
}

class SeparatedMenu
{
	private MenuItem[] items;

	public SeparatedMenu(MenuItem[] adults, MenuItem[] children)
	{
		items = Stream.concat(
			Stream.concat(Arrays.stream(adults), Arrays.stream(children)),
			Stream.<MenuItem>of(new SpecialMenu("child", children), new SpecialMenu("adult", adults))).toArray(MenuItem[]::new);
	}

	public void printItem(String name, Printer printer)
	{
		for(MenuItem item : items)
		{
			if(item.isName(name))
			{
				item.print(printer);
				break;
			}
		}
	}
}

public class Program
{
	public static void main(String[] args) throws Exception
	{
		String input;
		Scanner scanner = new Scanner(System.in);
		input = scanner.nextLine();
		scanner.close();

		new SeparatedMenu(
			new MenuItem[]
			{
				new FoodItem("Chicken", 300, 2500, 50),
				new DrinkItem("Tea", 150, 200, 10),
				new MusicItem("House on fire", "Sia", 1.5f),
			},
			new MenuItem[]
			{
				new FoodItem("Small chicken", 150, 1000, 30),
				new DrinkItem("Milk", 100, 250, 5),
				new MusicItem("Goodbye moonmen", "Unknown author", 2.5f),
			}
		).printItem(input, () ->
		{
			System.out.println();
			return System.out::println;
		});
	}
}