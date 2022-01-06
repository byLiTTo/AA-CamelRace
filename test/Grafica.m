clear all, clc

M = csvread("resultados.csv",0,0);
E = csvread("Epsilon.csv",0,0);
X = M(:,1); % Poblacion
Y1 = M(:,3); % Ticks
Y2 = E(:,1); % Epsilon

window = 4;
mediaVictorias = movmean(Y1,window);
figure,
plot(X,Y1,'-.g',X,mediaVictorias,'-b',X,Y2,'-r')
title(strcat('Resultados de Entrenamiento - k=',num2str(k)))
legend('Puntuación',strcat('Puntuación Media con Ventana-',num2str(window)),'Epsilon','best')
xlabel('Partidas')
ylabel('Epsilon')

