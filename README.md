Documentação de decisões técnicas e considerações relevantes:
    Criei 3 tabelas, sendo elas:
        pneu - será gravado as informações do pneu e da posição. Essa posição será validada no código se está reptindo a posição e o veículo.
        veículo - será gravado as informações do veículo e o tipo do veículo.
        tipo de veículo - será gravado o tipo do veículo com o intuito de fazer uma validação na quantidade permitida de pneus cadastrado por veículo.

    Foi criado CRUD padrão para as tabelas de pneu e veículo, como a tabela de tipo de veículo não é algo que é alterado muito essa tabela precisa ser populada via script.

    Como testar:
        Tabela veículo - Get - sera buscado todos os veículos sem os pneus informado de acordo com a paginação
            http://localhost:8080/vehicle?page=1&size=15
        Post - será gravado as informações do veículo
            http://localhost:8080/vehicle
            body:   {
                        "plate": "EDF",
                        "mark": "CBA",
                        "mileage": 3214,
                        "statusType": "AVAILABLE",
                        "vehicleType": {
                            "id": "7e6291b5-75d0-4f3e-9481-f8f3fe92b034"
                        }
                    }
        Put - será alterado os valores do veículo
            http://localhost:8080/vehicle/b735a488-54af-4b63-adc3-11f8e3439cea
            body:   {
                        "plate": "JAB3H56",
                        "mark": "Peugeot",
                        "mileage": 3214,
                        "statusType": "AVAILABLE",
                        "vehicleType": {
                            "id": "7e6291b5-75d0-4f3e-9481-f8f3fe92b034"
                        }
                    } 
        Delete - removera o registro
            http://localhost:8080/vehicle/b735a488-54af-4b63-adc3-11f8e3439cea
        
        Get - Busca individual do veículo irá trazer as informações dos pneus
            http://localhost:8080/vehicle/aa686165-0fde-4eff-84ef-754d5f457e43

        Tabela Pneu - Get - sera buscado todos os pneus de acordo com a paginação
            http://localhost:8080/tire
        Post - Será gravado as informações do pneu sem a posição e nem o veículo, pois tem uma API especifica pra isso.
            http://localhost:8080/tire
            body:   {
                        "positionNumber": 42131,
                        "position": "A",
                        "mark": "Marca test",
                        "pressure": 1231,
                        "statusType": "SCRAP"
                    }
        Put - Será alterado os valores do pneu
            http://localhost:8080/tire/e3542cbb-d36d-4597-b870-c7861786e76d
            body:   {
                        "positionNumber": 180,
                        "position": "C",
                        "mark": "Pirelli",
                        "pressure": 1231,
                        "statusType": "SCRAP"
                    }
        Delete - removera o registro
            http://localhost:8080/tire/e3542cbb-d36d-4597-b870-c7861786e76d

        Para vincular um pneu ao veículo, foi criado a API logo em baixo, nessa api precisa passar o id do veículo e no body as informações do pneu(id e posição), será feito validação se existe veículo, se as posições informada não estão vazia e nem reptida, se a posição já está gravada, se existe pneu cadastrado e se a quantidade de pneu passou do limite disponivel. Pode ser gravado mais de um pneu por vez
            http://localhost:8080/vehicle/880329af-77e0-472d-8cad-9cd16fec30b3
            body:   [
                        {
                            "id": "a8285fc6-6e57-4818-9dc9-f96f7f10f7e6",
                            "position": "A"
                        },
                        {
                            "id": "3c0d2ba3-cd85-4f25-90fb-7eaf1010bd95",
                            "position": "B"
                        }
                    ]

        Para desvincular o pneu do veículo, precisa passar o id do pneu na url da requisição
            http://localhost:8080/tire/0776edc7-7a95-42b4-b05f-aae6c4eae9d5
