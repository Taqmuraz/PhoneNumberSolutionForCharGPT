import java.io.PrintStream;
import java.util.Arrays;

interface Person
{
	void print(PrintStream stream);
	String getName();
}

class MortalPerson implements Person
{
	private String name;
	private int age;
	
	public MortalPerson(String name, int age)
	{
		this.name = name;
		this.age = age;
	}

	public void print(PrintStream stream)
	{
		stream.print("Name : ");
		stream.print(name);
		stream.print("Age : ");
		stream.print(age);
	}

	@Override
	public String getName()
	{
		return name;
	}
}

class GodPerson implements Person
{
	private String name;

	public GodPerson(String name) {
		this.name = name;
	}

	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public void print(PrintStream stream)
	{
		stream.print("Name : ");
		stream.print(name);
		stream.print(", he is God");
	}
}

public class Program
{
	public static void main(String[] args) throws Exception
	{
		Person[] persons =
		{
			new MortalPerson("Alice", 30),
			new GodPerson("Jesus"),
			new GodPerson("Allah"),
		};

		Arrays.stream(persons).sorted((a, b) -> a.getName().compareTo(b.getName())).forEach(person ->
		{
			person.print(System.out);
			System.out.println();
		});
	}
}