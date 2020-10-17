#ifndef TOAST_H
#define TOAST_H

#include <QObject>
#include <QString>

class Toast : public QObject
{
    Q_OBJECT
public:
    enum Duration {
        SHORT = 0,
        LONG = 1
    };
public:
    explicit Toast(QObject *parent = nullptr);
    void showToast(const QString &message, Toast::Duration duration = Toast::Duration::LONG);
signals:

};

#endif // TOAST_H
