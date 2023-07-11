# Vertex Chat:

### Project Info:
- author:       RiseGhost 👻
- Language:     Java ☕
- FrameWork:    JavaFX

Tem como objetivo a implementação de um chatroom que permita a partilha de ficheiros e mensagens.Este repositória não inclui o servidor. O servidor pode ser encontrado no seguinte link:

https://github.com/RiseGhost/VertexCLI

### Implementação:

Cada cliente tem dois threads a serem executados. Um thread responsável por gestão da interface gráfica e envio de mensagens/ficheiros. E outro thread responsável por fazer a receção dos packages oriundos do servidor.
O servidor e o cliente comunicão entre si através de array de bytes. 

### Files suport:
Os utilizadores podem enviar entre eles os seguintes tipos de ficheiros:
- jpg;
- jpeg;
- png;
- mp4;

Outro formatos de ficheiro não são suportados, uma vez que podem bugar a interface dos clientes.

### GUI:

Tela de login:

![](https://user-images.githubusercontent.com/91985039/252746458-43c69550-5036-4317-a6d5-d72913841e25.jpg)

Tela de login Erro to connect server:

![](https://user-images.githubusercontent.com/91985039/252746460-6b9e35f7-5d77-4c5f-8291-7185e0fb7071.jpg)

Tela de chat:

![](https://user-images.githubusercontent.com/91985039/252746451-a59127ac-0d4b-44f7-a825-8f332c2471b6.jpg)

### Problemas do projeto ❌:
- A lista de utilizadores conectados só é atualizada quando o cliente recebe alguma mensagem vinda de outro cliente.