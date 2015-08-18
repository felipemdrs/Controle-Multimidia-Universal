# encoding: utf-8
class User:
    name = "user"
    channel = None

    def __init__(self, name):
        self.name = name
        self.listFriends = {0:[]}  # dicionario que tem como chave o ranking de recomendações seguidas e o valor os users referente aquele ranking
        self.channel = None

    def getName(self):
        return self.name

    def addFriend(self, friend):  # esse friend eh um objeto user
        self.listFriends[0].append(friend)

    def newRanking(self, rankingCurrent, user):
        if not(self.listFriends.has_key(rankingCurrent + 1)):
            self.listFriends[rankingCurrent + 1] = []
        self.listFriends[rankingCurrent + 1].append(user)

    def upRanking(self, friend):
        for item in self.listFriends.items():
            listUsers = item[1]
            rankingTemp = item[0]
            if(friend in listUsers):
                listUsers.remove(friend)
                self.newRanking(rankingTemp, friend)
                break

    def getChannel(self):
        return self.channel

    def setChannel(self, channel):
        self.channel = channel

    def getSortFriends(self):
        retorno = []
        chaves = sorted(self.listFriends.keys())

        for i in range(len(chaves) - 1, -1, -1):
            for v in self.listFriends[chaves[i]]:
                retorno.append(v)

        return retorno
