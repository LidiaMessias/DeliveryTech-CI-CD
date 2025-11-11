package com.deliverytech.delivery_api.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.deliverytech.delivery_api.model.Cliente;
import com.deliverytech.delivery_api.model.Restaurante;
import com.deliverytech.delivery_api.repository.ClienteRepository;
//import com.deliverytech.delivery_api.repository.PedidoRepository;
//import com.deliverytech.delivery_api.repository.ProdutoRepository;
import com.deliverytech.delivery_api.repository.RestauranteRepository;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private RestauranteRepository restauranteRepository;

    /*@Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;*/

    @Override
    public void run(String... args) throws Exception {
        System.out.println("=== INICIANDO CARGA DE DADOS DE TESTE ===");

        if (clienteRepository.count() <= 3) {
            inserirClientes();
        } else {
            System.out.println("Clientes já existentes. Dados de teste não reinseridos.");
        }

        if (restauranteRepository.count() <= 3) {
            inserirRestaurantes();
        } else {
            System.out.println("Restaurantes já existentes. Dados de teste não reinseridos.");
        }

        //inserirProdutos();
        //inserirPedidos();
        testarConsultas();
    }

    private void inserirClientes() {

        System.out.println("Inserindo clientes de teste...");

        Cliente cliente1 = new Cliente();
        cliente1.setNome("George Clooney");
        cliente1.setEmail("gclooney@uol.com.br");
        cliente1.setTelefone("11999998888");
        cliente1.setEndereco("Rua das Bananeiras, 123");
        cliente1.setAtivo(true);

        Cliente cliente2 = new Cliente();
        cliente2.setNome("Scarlett Johansson");
        cliente2.setEmail("scarletjohn@outlook.com");
        cliente2.setTelefone("11988887777");
        cliente2.setEndereco("Avenida Paulista, 456");
        cliente2.setAtivo(true);

        clienteRepository.save(cliente1);
        clienteRepository.save(cliente2);

        System.out.println("2 Clientes de teste inseridos.");
    }

    private void inserirRestaurantes() {
        System.out.println("Inserindo restaurantes de teste...");

        Restaurante restaurante1 = new Restaurante();
        restaurante1.setNome("Pizzaria da Mama");
        restaurante1.setCategoria("Italiana");
        restaurante1.setEndereco("Rua Universo Paralelo, 1000");
        restaurante1.setTelefone("15922443366");
        restaurante1.setTaxaEntrega(new java.math.BigDecimal("5.00"));
        restaurante1.setAtivo(true);

        Restaurante restaurante2 = new Restaurante();
        restaurante2.setNome("Sushi do Yoshi");
        restaurante2.setCategoria("Japonesa");
        restaurante2.setEndereco("Avenida Japão, 789");
        restaurante2.setTelefone("15922334455");
        restaurante2.setTaxaEntrega(new java.math.BigDecimal("7.50"));
        restaurante2.setAtivo(true);

        restauranteRepository.save(restaurante1);
        restauranteRepository.save(restaurante2);

        System.out.println("Restaurantes de teste inseridos.");
    }

    private void testarConsultas() {
        System.out.println("\n== TESTANDO CONSULTAS DOS REPOSITORIES ==");

        // Teste ClienteRepository
        System.out.println("\nTestes ClienteRepository");

        var clientePorEmail = clienteRepository.findByEmail("scarletjohn@outlook.com");
        System.out.println("Cliente por email: " +
                clientePorEmail.map(Cliente::getNome).orElse("Não encontrado"));

        var clientesAtivos = clienteRepository.findByAtivoTrue();
        System.out.println("Clientes ativos: " + clientesAtivos.size());

        var clientesPorNome = clienteRepository.findByNomeContainingIgnoreCase("silva");
        System.out.println("Clientes com 'silva' no nome: " +
                clientesPorNome.size());

        boolean existeEmail = clienteRepository.existsByEmail("maria@email.com");
        System.out.println("Existe cliente com email: " + existeEmail);

        var restaurantesAtivos = restauranteRepository.findByAtivoTrue();
        System.out.println("\nRestaurantes ativos: " + restaurantesAtivos.size());

        var restaurantesPorCategoria = restauranteRepository.findByCategoria("Italiana");
        System.out.println("Restaurantes na categoria Italiana: " + restaurantesPorCategoria.size());

        var restaurantesPorTaxa = restauranteRepository.findByTaxaEntregaLessThanEqual(new java.math.BigDecimal("6.00"));
        System.out.println("Restaurantes com taxa de entrega <= 6.00: " + restaurantesPorTaxa.size());
    }

}
