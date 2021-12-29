
try:
    import matplotlib.pyplot as plt
    import os
    import sys

    # Leer nombre del fichero recibido por parámetro
    nombre = sys.argv[1]

    plt.figure()

    f = open(os.path.join(sys.path[0], nombre), 'r')
    costes = []
    for linea in f:
        costes.append(float(linea))
    f.close()

    plt.plot(costes)
    plt.plot(0, costes[0], 'bo')
    plt.plot(len(costes)-1, costes[-1], 'go')
    plt.legend(['Curva','Coste inicial: '+str(costes[0]), 'Coste final: '+str(costes[-1])])
    plt.xlabel('Iteraciones')
    plt.ylabel('Coste')
    plt.title('Resultados ' + nombre)

    plt.show()

except Exception as e:
    print(e)
    input('Pulsa enter para salir ...')
