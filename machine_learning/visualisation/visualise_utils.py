import matplotlib.pyplot as plt
import numpy as np


def scatter_predictions(y_test, y_predicted, save):
    fig, ax = plt.subplots()
    # a = plt.axes(aspect='equal')
    plt.scatter(y_test, y_predicted)
    plt.xlabel('True Values')
    plt.ylabel('Predictions')
    lims = [
        np.min([ax.get_xlim(), ax.get_ylim()]),  # min of both axes
        np.max([ax.get_xlim(), ax.get_ylim()]),  # max of both axes
    ]

    # now plot both limits against eachother
    ax.plot(lims, lims, 'k-', alpha=0.75, zorder=0)
    ax.set_aspect('equal')
    ax.set_xlim(lims)
    ax.set_ylim(lims)
    plt.savefig(save, format='pdf')
    plt.show()


def scatter_multiple_predictions(y_test, y_predicted, save, colors, markers, labels):
    fig, ax = plt.subplots()
    for i, pred in enumerate(y_predicted):
        ax.scatter(y_test, pred, color=colors[i], marker=markers[i], label=labels[i])
    lims = [
        np.min([ax.get_xlim(), ax.get_ylim()]),  # min of both axes
        np.max([ax.get_xlim(), ax.get_ylim()]),  # max of both axes
    ]
    plt.xlabel('True Values')
    plt.ylabel('Predictions')

    # now plot both limits against eachother
    ax.plot(lims, lims, 'k-', alpha=0.75, zorder=0)
    ax.set_aspect('equal')
    ax.set_xlim(lims)
    ax.set_ylim(lims)
    #ax.legend()
    plt.savefig(save, format='pdf')
    plt.show()
