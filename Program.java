import java.util.Arrays;
import java.util.stream.IntStream;

interface SymbolIterator
{
	boolean hasNext();
	char next();
}

interface Printer
{
	void print(char symbol);
}

interface Phone
{
	SymbolIterator symbols();
	void print(Printer printer);

	default boolean compare(Phone other)
	{
		SymbolIterator symbols = symbols();
		SymbolIterator otherSymbols = other.symbols();

		while(symbols.hasNext() && otherSymbols.hasNext())
		{
			if(symbols.next() != otherSymbols.next()) return false;
		}
		return symbols.hasNext() == otherSymbols.hasNext();
	}
}

class BufferedSymbolIterator implements SymbolIterator
{
	private char[] buffer;
	private int position;

	public BufferedSymbolIterator(char[] buffer)
	{
		this.buffer = buffer;
	}

	@Override
	public boolean hasNext()
	{
		return position < buffer.length;
	}

	@Override
	public char next()
	{
		return buffer[position++];
	}

	public void reset()
	{
		position = 0;
	}
}

class NumericPhone implements Phone
{
	private char[] symbols;

	public NumericPhone(int number)
	{
		symbols = String.valueOf(number).toCharArray();
	}

	@Override
	public SymbolIterator symbols()
	{
		return new BufferedSymbolIterator(symbols);
	}

	@Override
	public void print(Printer printer)
	{
		for(int i = 0; i < symbols.length; i++) printer.print(symbols[i]);
	}
}

class SeparatedPhone implements Phone
{
	private char[] symbols;
	private String source;

	public SeparatedPhone(String source, char separator)
	{
		this.source = source;
		Character[] symbols = Arrays.stream(source.split("\\" + Character.toString(separator))).<Character>flatMap(s ->
		{
			char[] chars = s.toCharArray();
			return IntStream.range(0, chars.length).boxed().map(i -> Character.valueOf(chars[i]));
		}).toArray(Character[]::new);
		this.symbols = new char[symbols.length];
		for(int i = 0; i < symbols.length; i++) this.symbols[i] = symbols[i];
	}
	@Override
	public SymbolIterator symbols()
	{
		return new BufferedSymbolIterator(symbols);
	}
	@Override
	public void print(Printer printer)
	{
		for(int i = 0; i < source.length(); i++) printer.print(source.charAt(i));
	}
}

class StringPhone implements Phone
{
	private char[] symbols;

	public StringPhone(String source)
	{
		symbols = source.toCharArray();
	}

	@Override
	public SymbolIterator symbols()
	{
		return new BufferedSymbolIterator(symbols);
	}

	@Override
	public void print(Printer printer)
	{
		for(int i = 0; i < symbols.length; i++) printer.print(symbols[i]);
	}
}

public class Program
{
	public static void main(String[] args) throws Exception
	{
		Phone[] phones =
		{
			new NumericPhone(911),
			new SeparatedPhone("9-11", '-'),
			new SeparatedPhone("9-1-1", '-'),
			new SeparatedPhone("9+11", '+'),
			new SeparatedPhone("9+1-1", '-'),
			new StringPhone("9+11"),
			new StringPhone("911A"),
			new SeparatedPhone("9-1-2", '-')
		};

		for(int i = 0; i < phones.length; i++)
		{
			for(int j = i; j < phones.length; j++)
			{
				Phone a = phones[i];
				Phone b = phones[j];
				a.print(System.out::print);
				System.out.print(" == ");
				b.print(System.out::print);
				System.out.println(" ? " + a.compare(b));
			}
		}
	}
}