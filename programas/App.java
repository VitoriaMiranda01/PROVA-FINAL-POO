import classes.Produto;
import classes.Venda;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.ArrayList;
import java.util.DoubleSummaryStatistics;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import javax.sound.midi.SysexMessage;

public class App {
    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(System.in);
        int opcao;
        List<Produto> produtosIncluidos = new ArrayList<>();
        //Cria o formato pra data
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
        do {
           System.out.println("==============");
           System.out.println("==== MENU ====");
           System.out.println("==============");
           System.out.println("1 - Incluir produto"); //OK
           System.out.println("2 - Consultar produto");
           System.out.println("3 - Listagem de produtos"); //OK
           System.out.println("4 - Vendas por período – detalhado");
           System.out.println("5 - Realizar venda");
           System.out.println("0 - Sair"); //OK
           System.out.print("Opção: ");

           opcao = in.nextInt();
           in.nextLine(); // Tira o ENTER que ficou na entrada na instrução anterior

//------------------------------------------------------------------------------------------------------------------------------------------------
           //1 - Digitar manualmente o código = tipo INT = 4 dígitos numéricos, 
           //2- caso não seja digitado isso, mostrar mensagem na tela = “Código inválido!” e pedir para digitar novamente. 
           //3- Caso seja digitado o código correto, printar na tela = Digite o nome do produto.
           if (opcao == 1) {
               Produto produto = new Produto();
               //Add o cóigo do produto
               int codigo = 0;
               boolean codigocorreto = false;
               do {
                   try { 
                       System.out.println("Digite o código do produto:");
                       produto.setCodigo(in.nextInt());
                       //codigo = in.nextInt();
                       in.nextLine();

                       //Verifica se já existe algum produto com esse código, se sim, pede para digitar de novo
                       if (ConfereCodigo(produtosIncluidos, codigo)) {
                           System.out.println("Este código já pertence a outro produto, por favor digite um diferente!\n");
                           continue;
                        }
                        codigocorreto= true;

                    } catch (InputMismatchException e) {
                        System.out.println("Código inválido, digite apenas números!");
                        in.nextLine();
                    }
                } while (codigocorreto == false);

               //Add o nome do produto
               System.out.println("O nome do produto:");
               produto.setNome(in.nextLine());
               //in.nextLine();

               //Add o valor do produto
               boolean valorcorreto = false;
               do {
                   try { 
                       System.out.println("O valor do produto:");
                       produto.setValor(in.nextDouble());
                       in.nextLine(); // Tira o ENTER que ficou na entrada na instrução anterior
                       valorcorreto = true;
                    } catch (InputMismatchException e) {
                       System.out.println("Valor inválido, digite apenas números!");
                       in.nextLine();
                    }
                } while (valorcorreto == false);
                
               //Add a quantidade do produto
               System.out.println("A quantidade do produto:");
               produto.setEstoque(in.nextInt());
               in.nextLine();

               //Add o produto no estoque
               produtosIncluidos.add(produto);
               System.out.println("Produto adicionado com sucesso!");

               voltarMenu(in);

//------------------------------------------------------------------------------------------------------------------------------------------------
            } else if (opcao == 2) {
                 if (produtosIncluidos.isEmpty()) {
                    System.out.println("\nNão há produtos cadastrados para exibir!");
                    voltarMenu(in);
                    continue;
                 }
                 System.out.println("------------------ -----");
                 System.out.println("-- CONSULTAR PRODUTO: --");
                 System.out.println("------------------ -----");
                
                 System.out.println("Digite o código do produto:");
                 int codigoDigitado = in.nextInt();
                 in.nextLine();

                 for(Produto prod : produtosIncluidos) { 
                     if (prod.getCodigo() == codigoDigitado) {
                         System.out.println("             ");
                         System.out.printf("Código do produto: %d \nNome do produto: %s \nValor unitário: R$ %.2f \nQuantiade em estoque; %d", 
                         prod.getCodigo(), prod.getNome(), prod.getValor(), prod.getEstoque());
                         System.out.println("             ");
                        } 
                    }
                    voltarMenu(in);

//-----------------------------------------------------------------------------------------------------------------------------------------------                           
            } else if (opcao == 3) { 
                 System.out.println(                       );
                 System.out.println("---------------------------");
                 System.out.println("-- LISTAGEM DE PRODUTOS: --");
                 System.out.println("---------------------------");
                 System.out.println(                       );
                 //Mostra o nome, código, valor e qtd em estoque do produto
                 System.out.println("DETALHE:");
                 for(Produto produto : produtosIncluidos) {
                 System.out.println("Nome do produto: " + produto.getNome());
                 System.out.println("Código do produto: " + produto.getCodigo());
                 System.out.println("Valor do produto: R$" + produto.getValor());
                 System.out.println("Estoque do produto: " + produto.getEstoque());
                 System.out.println(                       );
                }
                 //Obtem o produto com menor valor, o com valor médio e o prod. com maior valor 
                 System.out.println("RODAPÉ:");
                 DoubleSummaryStatistics resumo = produtosIncluidos.stream().collect(Collectors.summarizingDouble(Produto::getValor));
                 String valorMin = String.format("O menor valor é: R$%.2f", resumo.getMin());
                 String valorAvg = String.format("O valor médio é: R$%.2f", resumo.getAverage());
                 String valorMax = String.format("O maior valor é: R$%.2f", resumo.getMax());
                 //Imprime esses valoes
                 System.out.println(valorMin);
                 System.out.println(valorAvg);
                 System.out.println(valorMax);
                 System.out.println(        );

                 voltarMenu(in);
                
//----------------------------------------------------------------------------------------------------------------------------------------------
           } else if (opcao == 4) { 
                 System.out.println("-------------------------------------");
                 System.out.println("-- VENDAS POR PERÍODO - DETALHADO: --");
                 System.out.println("-------------------------------------");
                 System.out.println("                                      ");
                 System.out.println("Digite a data INICIAL do período no formato dd/MM/yyyy: ");
 
                 //Verifica a data INICIAL
                 String dataInicial;
                 do{
                     //1 - Crio uma String com a entrada digitada por ele. 2- Crio o Objeto LocalDate e converto a entrada do Usuário. 
                     dataInicial = in.nextLine();
                    } while(!dataInicial.matches( "\\d{2}/\\d{2}/\\d{4}"));
                 //O método parse() recebe uma String e converte para o padrão especificado a seguir
                 LocalDate dataNova = LocalDate.parse(dataInicial, formatter /* Esse é o padrão criado */);

                 System.out.println("Digite a data FINAL do período no formato dd/MM/yyyy: ");

                 //Verifica a data FINAL
                 String dataFinal;
                 do{
                     dataFinal = in.nextLine();
                 } while (!dataFinal.matches( "\\d{2}/\\d{2}/\\d{4}"));
                 LocalDate dataNova2 = LocalDate.parse(dataFinal, formatter /* Esse é o padrão criado */);
                 
                 //COMENTEI ESSA PARTE PRA NÃO DAR ERRO NO PROGRAMA, PQ EU NÃO CONSEGUI TERMINAR A TEMPO
                 //vendasfeitas.stream()
                 //.filter (vendaAtual -> !(vendaAtual.getDataVenda().isAfter(dataFinal) || vendaAtual.getDataVenda().isBefore(dataInicial)))
                 //.forEach(vendaAtual -> {
                 //Monta o  print para cada venda
                 //});

                 voltarMenu(in);
//------------------------------------------------------------------------------------------------------------------------------------------------           
           } else if (opcao == 5) {
                 //Para passar a data atual fazemos o mesmo porém sem o uso da entrada do usuário. Apenas usando o método now()
                 String dataAtual = LocalDate.now() //Recebe a data atual no formato padrão de datas
                 .format(formatter); //Converte a data recebida para o meu formato
                 //O método parse() recebe uma String e converte para o padrão especificado a seguir
                 LocalDate dataNova2 = LocalDate.parse(dataAtual, formatter /* Esse é o padrão criado */);  
 
                 voltarMenu(in);
            }
                
           else if (opcao != 0) {
           System.out.println("\nOpção inválida!");
           }
        } while (opcao != 0);

        System.out.println("Fim do programa!");

        in.close();
    }

    private static boolean ConfereCodigo(List<Produto> produtosIncluidos, int codigo) {
        for (Produto prod : produtosIncluidos) {
            if (prod.getCodigo() == codigo) {
                return true;
            }
        }
        return false;
    }
//------------------------------------------------------------------------------------------------------------------------------------------------
    private static void voltarMenu(Scanner in) throws InterruptedException, IOException {
        System.out.println("\nPressione ENTER para voltar ao menu.");
        in.nextLine();

        // Limpa toda a tela, deixando novamente apenas o menu
        if (System.getProperty("os.name").contains("Windows"))
            new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
        else
            System.out.print("\033[H\033[2J");

        System.out.flush();
    }
}